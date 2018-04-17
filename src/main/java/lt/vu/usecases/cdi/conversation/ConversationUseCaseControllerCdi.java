package lt.vu.usecases.cdi.conversation;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lt.vu.entities.Course;
import lt.vu.entities.Student;
import lt.vu.entities.University;
import lt.vu.usecases.cdi.dao.CourseDAO;
import lt.vu.usecases.cdi.dao.StudentDAO;
import lt.vu.usecases.cdi.dao.UniversityDAO;
import org.hibernate.Hibernate;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@Named
@ConversationScoped
@Slf4j
public class ConversationUseCaseControllerCdi implements Serializable {

    private static final String PAGE_INDEX_REDIRECT = "conversation-cdi?faces-redirect=true";

    private enum CURRENT_FORM {
        CREATE_STUDENT, CREATE_COURSE, CONFIRMATION
    }

    @Inject
    private EntityManager em;

    @Inject
    @Getter
    private Conversation conversation;

    @Inject
    private CourseDAO courseDAO;
    @Inject
    private StudentDAO studentDAO;
    @Inject
    private UniversityDAO universityDAO;


    @Getter
    @Setter
    private Course course = new Course();
    @Getter
    private Student student = new Student();
    @Getter
    @Setter
    private University university = new University();
    @Getter
    private List<Student> allStudents;
    @Getter
    private List<University> allUniversities;
    @Getter
    private List<Course> universityCourses;

    private CURRENT_FORM currentForm = CURRENT_FORM.CREATE_STUDENT;
    public boolean isCurrentForm(CURRENT_FORM form) {
        return currentForm == form;
    }

    @PostConstruct
    public void init() {
        loadAllStudents();
        loadAllUniversities();
    }

    /**
     * The first conversation step.
     */
    public void createStudent() {
        conversation.begin();
        loadUniversityCourses(university.getId());
        student.setUniversity(university);
        currentForm = CURRENT_FORM.CREATE_COURSE;
    }

    /**
     * The second conversation step.
     */
    public void createCourse(){
        student.getCourseList().add(course);
        currentForm = CURRENT_FORM.CONFIRMATION;
    }


    /**
     * The last conversation step.
     */
    @Transactional(Transactional.TxType.REQUIRED)
    public String ok() {
        try {
            studentDAO.create(student);
            em.flush();
            Messages.addGlobalInfo("Success!");
        } catch (OptimisticLockException ole) {
            // Other user was faster...
            Messages.addGlobalWarn("Please try again");
            log.warn("Optimistic Lock violated: ", ole);
        } catch (PersistenceException pe) {
            // Some problems with DB - most often this is programmer's fault.
            Messages.addGlobalError("Finita la commedia...");
            log.error("Error ending conversation: ", pe);
        }
        Faces.getFlash().setKeepMessages(true);
        conversation.end();
        return PAGE_INDEX_REDIRECT;
    }

    /**
     * The last (alternative) conversation step.
     */
    public String cancel() {
        conversation.end();
        return PAGE_INDEX_REDIRECT;
    }

    private void loadAllStudents() {
        allStudents = studentDAO.getAllStudents();
    }

    private void loadAllUniversities() {
        allUniversities = universityDAO.getAllUniversities();
    }

    private void loadUniversityCourses(int universityId) {
        universityCourses = courseDAO.getUniversityCourses(universityId);
    }
}
