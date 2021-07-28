package com.ATUserManagement.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
public class AllUserList {

    private String firstName;
    private String lastName;
    private String email;
    private int age;
    private String contactNumber;
    private List<String> tags;

}
