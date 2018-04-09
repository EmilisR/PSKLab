/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.vu.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.johnzon.mapper.JohnzonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "STUDENT")
@NamedQueries({
    @NamedQuery(name = "Student.findAll", query = "SELECT s FROM Student s"),
    @NamedQuery(name = "Student.findById", query = "SELECT s FROM Student s WHERE s.id = :id"),
    @NamedQuery(name = "Student.findByFirstName", query = "SELECT s FROM Student s WHERE s.firstName LIKE :firstName"),
    @NamedQuery(name = "Student.findByLastName", query = "SELECT s FROM Student s WHERE s.lastName LIKE :lastName"),
    @NamedQuery(name = "Student.findByRegistrationNo", query = "SELECT s FROM Student s WHERE s.registrationNo = :registrationNo")
})
@Getter
@Setter
@EqualsAndHashCode(of = "registrationNo")
@ToString(of = {"id", "firstName", "lastName", "registrationNo"})
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Size(min = 3, max = 20)
    @Column(name = "FIRST_NAME")
    private String firstName;

    @Size(min = 3, max = 20)
    @Column(name = "LAST_NAME")
    private String lastName;

    @Size(min = 3, max = 20)
    @Column(name = "REGISTRATION_NO")
    private String registrationNo;

    @Version
    @Column(name = "OPT_LOCK_VERSION")
    private Integer optLockVersion;

    @JoinTable(name = "STUDENT_COURSE", joinColumns = {
        @JoinColumn(name = "STUDENT_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "COURSE_ID", referencedColumnName = "ID")})
    @ManyToMany
    @JohnzonIgnore
    private List<Course> courseList = new ArrayList<>();

    @JoinColumn(name = "UNIVERSITY_ID", referencedColumnName = "ID")
    @ManyToOne
    @JohnzonIgnore
    private University university;

}
