package com.ATUserManagement.controller;

import com.ATUserManagement.entity.*;
import com.ATUserManagement.exceptions.UserExistException;
import com.ATUserManagement.exceptions.UserNotFoundException;
import com.ATUserManagement.repository.UserRepository;
import com.ATUserManagement.service.impl.NumberServiceImpl;
import com.ATUserManagement.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/user-management")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void createOneUser(@RequestBody AddUserRequest addUserRequest)
            throws UserExistException {

        if (!userRepository.findById(addUserRequest.getEmail()).isPresent()) {
            User userNew = userServiceImpl.addNewUser(addUserRequest);
            userRepository.save(userNew);
        } else {
            throw new UserExistException("User " + "{" + addUserRequest.getEmail() + "}" + " already Exist!");
        }

    }

    @GetMapping("/user/{email}")
    @ResponseBody
    public UserDetails listOneUserDetail(@PathVariable(value = "email") String email)
            throws UserNotFoundException {
        User user =
                userRepository
                        .findById(email)
                        .orElseThrow(() -> new UserNotFoundException("User not found by this username : " + "{" + email + "}"));
        UserDetails userDetails = userServiceImpl.getOneUserDetails(user);

        return userDetails;
    }

    @GetMapping("/user/list")
    @ResponseBody
    public UserPaginationResponse listAllUserInPages(@RequestParam int page, @RequestParam int pageSize) {

        Pageable paging = PageRequest.of(page, pageSize);
        Page<User> pageTuts;
        pageTuts = userRepository.findAll(paging);

        List<User> usersInRepository = pageTuts.getContent();
        List<UserSummary> userSummary = userServiceImpl.listUsers(usersInRepository);

        UserPaginationResponse response = new UserPaginationResponse();
        response.setPage(page);
        response.setPageSize(pageSize);
        response.setTotalPage(pageTuts.getTotalPages());
        response.setUsers(userSummary);

        return response;
    }

    @DeleteMapping("/user/{email}")
    @ResponseBody
    public void deleteOneUser(@PathVariable(value = "email") String email)
            throws UserNotFoundException {
        User user =
                userRepository
                        .findById(email)
                        .orElseThrow(() -> new UserNotFoundException("User(to delete) not found by this username : " + "{" + email + "}"));
        userRepository.deleteById(email);
    }

    @PutMapping("/user")
    @ResponseBody
    public void UpdateUserRequest(@RequestBody AddUserRequest addUserRequest)
            throws UserNotFoundException {
        User userToUpdate =
                userRepository
                        .findById(addUserRequest.getEmail())
                        .orElseThrow(() -> new UserNotFoundException("User(to update) not found by this username : " + "{" + addUserRequest.getEmail() + "}"));
        User user = userServiceImpl.updateOneUser(userToUpdate, addUserRequest);

        userRepository.save(user);
    }

}
