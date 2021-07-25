package com.ATUserManagement.controller;

import com.ATUserManagement.entity.User;
import com.ATUserManagement.entity.User_detail_process;
import com.ATUserManagement.exceptions.GlobalExceptionHandler;
import com.ATUserManagement.exceptions.UserExistException;
import com.ATUserManagement.exceptions.UserNotFoundException;
import com.ATUserManagement.repository.UserRepository;
import com.ATUserManagement.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    public void addUser(@RequestBody User_detail_process user_detail_process)
            throws UserExistException {

        if (!userRepository.findById(user_detail_process.getEmail()).isPresent()) {
            User user = userServiceImpl.addNewUser(user_detail_process);
            userRepository.save(user);
        } else {
            throw new UserExistException("User " + "{" + user_detail_process.getEmail() + "}" + " already Exist!");
        }

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
    @GetMapping("/user/list")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> findUser(@RequestParam int page, @RequestParam int pageSize) {
        List<User> users = new ArrayList<User>();
        Pageable paging = PageRequest.of(page, pageSize);

        Page<User> pageTuts;
        pageTuts = userRepository.findAll(paging);
        users = pageTuts.getContent();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("page", page);
        response.put("pageSize", pageSize);
        response.put("totalPages", pageTuts.getTotalPages());
        response.put("user", users);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


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
    public void updateUser(@RequestBody User_detail_process user_update)
            throws UserNotFoundException {
        User user_to_update =
                userRepository
                        .findById(user_update.getEmail())
                        .orElseThrow(() -> new UserNotFoundException("User(to update) not found by this username : " + "{" + user_update.getEmail() + "}"));
        User user = userServiceImpl.updateOneUser(user_to_update, user_update);

        userRepository.save(user);
    }
}
