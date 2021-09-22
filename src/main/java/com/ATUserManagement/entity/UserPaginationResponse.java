package com.ATUserManagement.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserPaginationResponse {

    private int page;
    private int pageSize;
    private int totalPage;
    private List<UserSummary> users;

}
