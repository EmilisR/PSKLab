package lt.vu.usecases.mybatis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Student {

    private Integer id;
    private String firstName;
    private String lastName;
    private Integer universityId;
    private String registrationNo;

    // Rankomis prirašyti:
    private University university;
    private List<Course> courses;
}