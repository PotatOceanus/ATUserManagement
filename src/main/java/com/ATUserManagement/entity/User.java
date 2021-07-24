package com.ATUserManagement.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="AT_user")
public class User {

    @Id
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String contactNumber;
    private int age;
    private String gender;
    private String nationality;
    private String tags;
    private String status;
    @EqualsAndHashCode.Exclude private String created;
    @EqualsAndHashCode.Exclude private String updated;

    @Override
    public String toString() {
        return "{"
                + "\"username\"" + ":\"" + username + "\","
                + "\"password\"" + ":\"" +password + "\","
                + "\"firstName\"" + ":\"" +firstName + "\","
                + "\"lastName\"" + ":\"" +lastName + "\","
                + "\"email\"" + ":\"" +email + "\","
                + "\"contactNumber\"" + ":\"" + contactNumber + "\","
                + "\"age\"" + ":" + age + ","
                + "\"gender\"" + ":\"" + gender + "\","
                + "\"nationality\"" + ":\"" + nationality + "\","
                + "\"tags\"" + ":\"" + tags + "\","
                + "\"status\"" + ":\"" + status + "\","
                + "\"created\"" + ":\"" + created + "\","
                + "\"updated\"" + ":\"" + updated + "\""
                + "}";
    }

}
