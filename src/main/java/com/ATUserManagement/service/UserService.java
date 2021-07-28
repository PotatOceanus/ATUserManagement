package com.ATUserManagement.service;

import com.ATUserManagement.entity.User;
import com.ATUserManagement.entity.UserDetailProcess;

public interface UserService {

    public User addNewUser(UserDetailProcess user_detail_process);

    public User updateOneUser(User user_to_update, UserDetailProcess user_detail_process);
}
