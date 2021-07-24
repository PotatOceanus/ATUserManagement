package com.ATUserManagement.service;

import com.ATUserManagement.entity.User;
import com.ATUserManagement.entity.User_detail_process;

public interface AddUserService {
    public User addNewUser(User_detail_process user_detailprocess);
}
