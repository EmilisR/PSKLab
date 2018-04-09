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

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ACCOUNT")
@Getter
@Setter
@EqualsAndHashCode(of = "userName")
@ToString(of = {"id", "userName", "firstName", "lastName"})
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    public Integer id;

    @Size(min = 3, max = 128)
    @Column(name = "USER_NAME")
    public String userName;

    @Column(name = "PASSWORD_DIGEST")
    public String passwordDigest;

    @Column(name = "FIRST_NAME")
    public String firstName;

    @Column(name = "LAST_NAME")
    public String lastName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="ACCOUNT_GROUP", joinColumns=@JoinColumn(name="ACCOUNT_ID"))
    @Column(name = "GROUP_NAME")
    private Set<String> groups = new HashSet<>();
}
