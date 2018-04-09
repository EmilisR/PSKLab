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
@Table(name = "ACCOUNT_GROUP")
@Getter
@Setter
@EqualsAndHashCode(of = "groupName")
@ToString(of = {"accountId", "groupName"})
public class AccountGroup implements Serializable {

    @Id
    @Column(name = "ACCOUNT_ID")
    public Integer accountId;

    @Size(min = 3, max = 25)
    @Column(name = "GROUP_NAME")
    public String groupName;


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="ACCOUNT", joinColumns=@JoinColumn(name="ID"))
    @Column(name = "USER_NAME")
    private Set<String> users = new HashSet<>();
}
