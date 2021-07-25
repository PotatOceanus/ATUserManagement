package com.ATUserManagement.controller;

import com.ATUserManagement.entity.User;
import com.ATUserManagement.entity.User_detail_process;
import com.ATUserManagement.exceptions.GlobalExceptionHandler;
import com.ATUserManagement.exceptions.UserNotFoundException;
import com.ATUserManagement.repository.UserRepository;
import com.ATUserManagement.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-management")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;
//
//    @Autowired
//    private UpdateUserServiceImpl updateUserServiceImpl;

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void addUser(@RequestBody User_detail_process user_detail_process) {
        User user = userServiceImpl.addNewUser(user_detail_process);
        userRepository.save(user);
    }

//    @GetMapping("/find/{userName}")
//    @ResponseBody
//    public User findOneUser(@PathVariable(value = "userName") String username)
//            throws UserNotFoundException {
//        User user =
//                userRepository
//                        .findById(username)
//                        .orElseThrow(() -> new UserNotFoundException("User not found by this username : " + "{" + username + "}"));
//        return user;
//    }
//
//    @GetMapping("/find/all")
//    @ResponseBody
//    public List<User> findUser() {
//        return userRepository.findAll();
//    }


    @DeleteMapping("/user/{userName}")
    @ResponseBody
    public void deleteOneUser(@PathVariable(value = "userName") String username)
            throws UserNotFoundException {
        User user =
                userRepository
                        .findById(username)
                        .orElseThrow(() -> new UserNotFoundException(""));
        userRepository.deleteById(username);
    }

    @PutMapping("/user")
    @ResponseBody
    public GlobalExceptionHandler updateUser(@RequestBody User_detail_process user_update)
            throws UserNotFoundException {
        System.out.println(user_update.getEmail());
        User user_to_update =
                userRepository
                        .findById(user_update.getEmail())
                        .orElseThrow(() -> new UserNotFoundException("User(to update) not found by this username : " + "{" + user_update.getEmail() + "}"));
        User user = userServiceImpl.updateOneUser(user_to_update, user_update);

        userRepository.save(user);

        return new GlobalExceptionHandler();
    }
}
