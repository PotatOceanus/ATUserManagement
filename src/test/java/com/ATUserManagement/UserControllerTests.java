package com.ATUserManagement;

import com.ATUserManagement.controller.UserController;
import com.ATUserManagement.entity.AllUserList;
import com.ATUserManagement.entity.OneUserDetail;
import com.ATUserManagement.entity.User;
import com.ATUserManagement.entity.UserDetailProcess;
import com.ATUserManagement.exceptions.UserExistException;
import com.ATUserManagement.exceptions.UserNotFoundException;
import com.ATUserManagement.repository.UserRepository;
import com.ATUserManagement.service.impl.UserServiceImpl;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class UserControllerTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserServiceImpl userServiceImpl;

    @InjectMocks
    UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setupMock() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    User userFirst = new User("FirstTester@test.com",
            "123456",
            "First",
            "Tester",
            "FirstTester@test.com",
            "654321",
            30,
            "male",
            "US",
            "Tester:01:UserController",
            "active",
            "2021-07-28T22:10:36.858Z",
            "2021-07-28T22:10:36.858Z");

    User userSecond = new User("SecondTester@test.com",
            "234567",
            "Second",
            "Tester",
            "SecondTester@test.com",
            "765432",
            31,
            "female",
            "TW",
            "Tester:02:UserController",
            "active",
            "2021-07-28T23:10:36.858Z",
            "2021-07-28T23:10:36.858Z");


    @Test
    public void createOneUserTestError () throws Exception {

        UserDetailProcess userCreateDetail = new UserDetailProcess();
        userCreateDetail.setPassword("123456");
        userCreateDetail.setFirstName("First");
        userCreateDetail.setLastName("Tester");
        userCreateDetail.setEmail("FirstTester@test.com");
        userCreateDetail.setContactNumber("654321");
        userCreateDetail.setTags(Arrays.asList("Tester","01","UserController"));

        Mockito.when(userRepository.findById("FirstTester@test.com")).thenReturn(Optional.of(userFirst));

        MvcResult result = mockMvc.perform(post("/api/user-management/user")
                .content(JSON.toJSONString(userCreateDetail))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();
        assertEquals(302, result.getResponse().getStatus());
        verify(userRepository, times(0)).save(userFirst);
    }

    @Test
    public void createOneUserTestNormal () throws Exception {

        UserDetailProcess userCreateDetail = new UserDetailProcess();
        userCreateDetail.setPassword("123456");
        userCreateDetail.setFirstName("First");
        userCreateDetail.setLastName("Tester");
        userCreateDetail.setEmail("FirstTester@test.com");
        userCreateDetail.setContactNumber("654321");
        userCreateDetail.setTags(Arrays.asList("Tester","01","UserController"));

        doReturn(userFirst).when(userServiceImpl).addNewUser(any(UserDetailProcess.class));

        MvcResult result = mockMvc.perform(post("/api/user-management/user")
                .content(JSON.toJSONString(userCreateDetail))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();
        assertEquals(201, result.getResponse().getStatus());
        verify(userRepository, times(1)).save(userFirst);
    }


    @Test
    public void listOneUserDetailError () throws Exception {

        String email = "FirstTester@test.com";
        Mockito.when(userRepository.findById(email)).thenReturn(Optional.empty());

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/user-management/user/{email}","FirstTester@test.com"))
                .andDo(print())
                .andReturn();
        assertEquals(404, result.getResponse().getStatus());
        assertEquals("User not found by this username : " + "{" + "FirstTester@test.com" + "}", result.getResolvedException().getMessage());
    }

    @Test
    public void listOneUserDetailNormal () throws Exception {

        String email = "FirstTester@test.com";
        Mockito.when(userRepository.findById(email)).thenReturn(Optional.of(userFirst));
        when(userServiceImpl.getOneUserDetails(userFirst)).thenCallRealMethod();

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/user-management/user/{email}","FirstTester@test.com"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        OneUserDetail oneUserDetail = userServiceImpl.getOneUserDetails(userFirst);
        assertEquals(oneUserDetail.toString(), userController.listOneUserDetail("FirstTester@test.com").toString());
    }

    @Test
    public void listAllUserInPagesTests () throws Exception {

        int page = 1;
        int pageSize = 2;
        Pageable paging = PageRequest.of(page, pageSize);

        List<User> usersInRepository = new ArrayList<>();
        usersInRepository.add(userFirst);
        usersInRepository.add(userSecond);
        Page<User> pageTut = new PageImpl<User>(usersInRepository,paging,1);

        doReturn(pageTut).when(userRepository).findAll(paging);
        when(userServiceImpl.listUsers(usersInRepository)).thenCallRealMethod();

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/user-management/user/list?page=1&pageSize=2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<AllUserList> allUserLists = userServiceImpl.listUsers(usersInRepository);
        assertEquals(allUserLists.toString(), userController.listAllUserInPages(1,2).getBody().get("user").toString());
    }

    @Test
    public void deleteOneUserNormal() throws Exception {

        Mockito.when(userRepository.findById("FirstTester@test.com")).thenReturn(java.util.Optional.of(userFirst));

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/user-management/user/{email}","FirstTester@test.com"))
                .andDo(print())
                .andReturn();

        assertEquals(200,result.getResponse().getStatus());
        verify(userRepository, times(1)).deleteById("FirstTester@test.com");
    }

    @Test
    public void deleteOneUserError() throws Exception {
        String email = "FirstTester@test.com";
        Mockito.when(userRepository.findById(email)).thenReturn(Optional.empty());

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/user-management/user/{email}",email))
                .andDo(print())
                .andReturn();

        assertEquals(404, result.getResponse().getStatus());
        assertEquals("User(to delete) not found by this username : " + "{" +email + "}", result.getResolvedException().getMessage());
    }

    @Test
    public void updateOneUserNormal() throws Exception {
        String email = "FirstTester@test.com";
        Mockito.when(userRepository.findById(email)).thenReturn(java.util.Optional.of(userFirst));

        UserDetailProcess userDetailProcess = new UserDetailProcess();
        userDetailProcess.setPassword("345678");
        userDetailProcess.setFirstName("Third");
        userDetailProcess.setLastName("Tester");
        userDetailProcess.setEmail("FirstTester@test.com");
        userDetailProcess.setContactNumber("876543");
        userDetailProcess.setTags(Arrays.asList("Tester","03","UserController"));

        User userThird = new User("FirstTester@test.com",
                "345678",
                "Third",
                "Tester",
                "FirstTester@test.com",
                "876543",
                30,
                "male",
                "US",
                "Tester:03:UserController",
                "active",
                "2021-07-28T22:10:36.858Z",
                "2021-07-28T22:10:36.858Z");
        doReturn(userThird).when(userServiceImpl).updateOneUser(any(User.class),any(UserDetailProcess.class));

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/user-management/user")
                        .content(JSON.toJSONString(userDetailProcess))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        verify((userRepository),times(1)).findById(email);
        verify((userRepository),times(1)).save(userThird);
    }

    @Test
    public void updateOneUserError() throws Exception {
        String email = "FirstTester@test.com";
        Mockito.when(userRepository.findById(email)).thenReturn(Optional.empty());

        UserDetailProcess userDetailProcess = new UserDetailProcess();
        userDetailProcess.setPassword("345678");
        userDetailProcess.setFirstName("Third");
        userDetailProcess.setLastName("Tester");
        userDetailProcess.setEmail("FirstTester@test.com");
        userDetailProcess.setContactNumber("876543");
        userDetailProcess.setTags(Arrays.asList("Tester","03","UserController"));

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/user-management/user")
                        .content(JSON.toJSONString(userDetailProcess))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        assertEquals(404, result.getResponse().getStatus());
        assertEquals("User(to update) not found by this username : " + "{" + email + "}", result.getResolvedException().getMessage());
    }

}
