package lt.vu.usecases.ejb;

import lombok.Getter;
import lt.vu.entities.Course;
import lt.vu.entities.Student;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Named
@ConversationScoped
@Stateful
public class ConversationUseCaseControllerEjb implements Serializable {

    private static final String PAGE_INDEX_REDIRECT = "conversation-ejb?faces-redirect=true";

    private enum CURRENT_FORM {
        CREATE_COURSE, CREATE_STUDENT, CONFIRMATION
    }

    @PersistenceContext(type = PersistenceContextType.EXTENDED, synchronization = SynchronizationType.UNSYNCHRONIZED)
    private EntityManager em;

    @Inject
    @Getter
    private Conversation conversation;

    @Inject
    private CourseEjbDAO courseEjbDAO;
    @Inject
    private StudentEjbDAO studentEjbDAO;

    @Getter
    private Course course = new Course();
    @Getter
    private Student student = new Student();

    private CURRENT_FORM currentForm = CURRENT_FORM.CREATE_COURSE;
    public boolean isCurrentForm(CURRENT_FORM form) {
        return currentForm == form;
    }

    /**
     * The first conversation step.
     */
    public void createCourse() {
        conversation.begin();
        currentForm = CURRENT_FORM.CREATE_STUDENT;
    }

    /**
     * The second conversation step.
     */
    public void createStudent() {
        student.getCourseList().add(course);
        course.getStudentList().add(student);
        currentForm = CURRENT_FORM.CONFIRMATION;
    }

    /**
     * The last conversation step.
     */
    public String ok() {
        try {
            courseEjbDAO.create(course);
            studentEjbDAO.create(student);
            em.joinTransaction();
            em.flush();
            Messages.addGlobalInfo("Success!");
        } catch (OptimisticLockException ole) {
            // Other user was faster...
            Messages.addGlobalWarn("Please try again");
        } catch (PersistenceException pe) {
            // Some problems with DB - most often this is programmer's fault.
           Messages.addGlobalError("Finita la commedia...");
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

    public List<Student> getAllStudents() {
        return studentEjbDAO.getAllStudents();
    }
}
