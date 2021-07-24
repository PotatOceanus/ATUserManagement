package com.ATUserManagement.service;

import com.ATUserManagement.entity.User;
import com.ATUserManagement.entity.User_detail_process;

public interface UserService {

    public User addNewUser(User_detail_process user_detail_process);

    public User updateOneUser(User user_to_update, User_detail_process user_detail_process);
}
