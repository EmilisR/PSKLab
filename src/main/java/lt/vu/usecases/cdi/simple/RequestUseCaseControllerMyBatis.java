package lt.vu.usecases.cdi.simple;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lt.vu.usecases.mybatis.dao.CourseMapper;
import lt.vu.usecases.mybatis.dao.StudentCourseMapper;
import lt.vu.usecases.mybatis.dao.StudentMapper;
import lt.vu.usecases.mybatis.model.Course;
import lt.vu.usecases.mybatis.model.Student;
import lt.vu.usecases.mybatis.model.StudentCourse;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@Model // tas pats kaip: @Named ir @RequestScoped
@Slf4j
public class RequestUseCaseControllerMyBatis {

    @Getter
    private Course course = new Course();
    @Getter
    private Student student = new Student();
    @Getter
    private List<Student> allStudents;

    @PostConstruct
    public void init() {
        loadAllStudents();
    }

    @Inject
    private StudentMapper studentMapper;
    @Inject
    private CourseMapper courseMapper;
    @Inject
    private StudentCourseMapper studentCourseMapper;

    @Transactional
    public void createCourseStudent() {
        courseMapper.insert(course);
        studentMapper.insert(student);
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setCourseId(course.getId());
        studentCourse.setStudentId(student.getId());
        studentCourseMapper.insert(studentCourse);
        log.info("Maybe OK...");
    }

    private void loadAllStudents() {
        allStudents = studentMapper.selectAll();
    }
}
