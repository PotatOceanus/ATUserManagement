package com.ATUserManagement.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class User_detail_process {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String contactNumber;
    private int age;
    private String gender;
    private String nationality;
    private String status;
    @EqualsAndHashCode.Exclude private String created;
    @EqualsAndHashCode.Exclude private String updated;

    private List<String> tags;
    private List<HashMap> country;

//    @Override
//    public String toString() {
//        return "{"
//                + "\"username\"" + ":\"" + username + "\","
//                + "\"password\"" + ":\"" +password + "\","
//                + "\"firstName\"" + ":\"" +firstName + "\","
//                + "\"lastName\"" + ":\"" +lastName + "\","
//                + "\"email\"" + ":\"" +email + "\","
//                + "\"contactNumber\"" + ":\"" + contactNumber + "\","
//                + "\"age\"" + ":" + age + ","
//                + "\"gender\"" + ":\"" + gender + "\","
//                + "\"nationality\"" + ":\"" + nationality + "\","
//                + "\"tags\"" + ":\"" + tags + "\","
//                + "\"status\"" + ":\"" + status + "\","
//                + "\"created\"" + ":\"" + created + "\","
//                + "\"updated\"" + ":\"" + updated + "\""
//                + "}";
//    }

}
