package com.ATUserManagement.service;

import com.ATUserManagement.entity.AddUserRequest;
import com.ATUserManagement.entity.User;
import com.ATUserManagement.entity.UserDetailProcess;

public interface UserService {

    public User addNewUser(AddUserRequest addUserRequest);

    public User updateOneUser(User user_to_update, AddUserRequest addUserRequest);
}
