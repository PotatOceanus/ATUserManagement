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
public class UserSummary {

    private String firstName;
    private String lastName;
    private String email;
    private int age;
    private String contactNumber;
    private List<String> tags;

    @Override
    public String toString() {
        String tags_print = "";
        for (int i = 0;i < tags.size() - 1; i++) {
            tags_print = tags_print + "\"" + tags.get(i) + "\",";
        }
        tags_print = tags_print + "\"" + tags.get(tags.size() - 1) + "\"";

        return "{"
                + "\"firstName\"" + ":\"" +firstName + "\","
                + "\"lastName\"" + ":\"" +lastName + "\","
                + "\"email\"" + ":\"" +email + "\","
                + "\"age\"" + ":" + age + ","
                + "\"contactNumber\"" + ":\"" + contactNumber + "\","
                + "\"tags\"" + ":[" + tags_print + "]"
                + "}";
    }

}
