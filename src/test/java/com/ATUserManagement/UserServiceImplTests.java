package com.ATUserManagement;

import com.ATUserManagement.entity.UserSummary;
import com.ATUserManagement.entity.UserDetails;
import com.ATUserManagement.entity.User;
import com.ATUserManagement.entity.UserDetailProcess;
import com.ATUserManagement.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceImplTests {

    @Mock
    private UserDetailProcess userCreateDetail;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private UserServiceImpl userServiceImpl;

    @InjectMocks
    private UserServiceImpl userServiceImplInject;

    @BeforeEach
    public void setupMock() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addNewUserTest () {

        UserDetailProcess userCreateDetail = new UserDetailProcess();
        userCreateDetail.setPassword("123456");
        userCreateDetail.setFirstName("First");
        userCreateDetail.setLastName("Tester");
        userCreateDetail.setEmail("FirstTest@test.com");
        userCreateDetail.setContactNumber("654321");
        userCreateDetail.setTags(Arrays.asList("Test","Case","01","addUser"));

        User userDetailGuess = new User();
        userDetailGuess.setAge(30);
        userDetailGuess.setGender("male");
        userDetailGuess.setNationality("TW");
        doReturn(userDetailGuess).when(userServiceImpl).detailGuess(any());

        User userDetailGenerate = new User();
        userDetailGenerate.setUsername("FirstTest@test.com");
        userDetailGenerate.setTags("Test:Case:01:addUser");
        userDetailGenerate.setStatus("active");
        doReturn(userDetailGenerate).when(userServiceImpl).detailGenerate(any());

        when(userServiceImpl.addNewUser(userCreateDetail)).thenCallRealMethod();
        User user = userServiceImpl.addNewUser(userCreateDetail);

        assertEquals("FirstTest@test.com",user.getUsername());
        assertEquals("123456",user.getPassword());
        assertEquals("First",user.getFirstName());
        assertEquals("Tester",user.getLastName());
        assertEquals("FirstTest@test.com",user.getEmail());
        assertEquals("654321",user.getContactNumber());
        assertEquals(30,user.getAge());
        assertEquals("male",user.getGender());
        assertEquals("TW",user.getNationality());
        assertEquals("Test:Case:01:addUser",user.getTags());
        assertEquals("active",user.getStatus());
    }


    @Test
    public void updateOneUserTest () {

        UserDetailProcess userUpdateDetail = new UserDetailProcess();
        userUpdateDetail.setPassword("234567");
        userUpdateDetail.setFirstName("Second");
        userUpdateDetail.setLastName("Tester");
        userUpdateDetail.setEmail("FirstTest@test.com");
        userUpdateDetail.setContactNumber("765432");
        userUpdateDetail.setTags(Arrays.asList("Test","Case","02","updateUser"));

        User userToUpdate = new User();
        userToUpdate.setUsername("FirstTester@test.com");
        userToUpdate.setPassword("123456");
        userToUpdate.setFirstName("First");
        userToUpdate.setLastName("Tester");
        userToUpdate.setEmail("FirstTester@test.com");
        userToUpdate.setContactNumber("654321");
        userToUpdate.setTags("Test:Case:01:addUser");
        userToUpdate.setAge(30);
        userToUpdate.setGender("male");
        userToUpdate.setNationality("TW");
        userToUpdate.setStatus("active");

        User userDetailGuess = new User();
        userDetailGuess.setAge(31);
        userDetailGuess.setGender("female");
        userDetailGuess.setNationality("US");
        doReturn(userDetailGuess).when(userServiceImpl).detailGuess(any());

        User userDetailGenerate = new User();
        userDetailGenerate.setUsername("FirstTester@test.com");
        userDetailGenerate.setTags("Test:Case:02:updateUser");
        userDetailGenerate.setStatus("active");
        doReturn(userDetailGenerate).when(userServiceImpl).detailGenerate(any());

        when(userServiceImpl.updateOneUser(userToUpdate,userUpdateDetail)).thenCallRealMethod();
        User user = userServiceImpl.updateOneUser(userToUpdate,userUpdateDetail);

        assertEquals("FirstTester@test.com",user.getUsername());
        assertEquals("234567",user.getPassword());
        assertEquals("Second",user.getFirstName());
        assertEquals("Tester",user.getLastName());
        assertEquals("FirstTester@test.com",user.getEmail());
        assertEquals("765432",user.getContactNumber());
        assertEquals(31,user.getAge());
        assertEquals("female",user.getGender());
        assertEquals("US",user.getNationality());
        assertEquals("Test:Case:02:updateUser",user.getTags());
        assertEquals("active",user.getStatus());
    }

    @Test
    public void detailGenerateTest () {

        UserDetailProcess userDetailPost = new UserDetailProcess();
        userDetailPost.setEmail("ThirdTester@test.com");
        userDetailPost.setTags(Arrays.asList("Test","Case","03","detailGenerate"));

        when(userServiceImpl.detailGenerate(userDetailPost)).thenCallRealMethod();
        User userDetailGenerate = userServiceImpl.detailGenerate(userDetailPost);

        assertEquals("ThirdTester@test.com", userDetailGenerate.getUsername());
        assertEquals("Test:Case:03:detailGenerate", userDetailGenerate.getTags());
        assertEquals("active",userDetailGenerate.getStatus());
    }


    @Test
    public void detailGuessTest () {

        String firstName = "Jack";

        List<HashMap> country = new ArrayList<>();
        HashMap map = new HashMap() ;
        map.put("probability",0.07);
        map.put("country_id","US");
        country.add(map);
        UserDetailProcess userResponse = new UserDetailProcess();
        userResponse.setAge(30);
        userResponse.setGender("male");
        userResponse.setCountry(country);
        doReturn(userResponse).when(restTemplate).getForObject("https://api.agify.io/?name={?}", UserDetailProcess.class,firstName);
        doReturn(userResponse).when(restTemplate).getForObject("https://api.genderize.io?name={name}", UserDetailProcess.class,firstName);
        doReturn(userResponse).when(restTemplate).getForObject("https://api.nationalize.io?name={name}", UserDetailProcess.class,firstName);

        User userDetailGuess = userServiceImplInject.detailGuess(firstName);

        assertEquals(30,userDetailGuess.getAge());
        assertEquals("male",userDetailGuess.getGender());
        assertEquals("US",userDetailGuess.getNationality());
    }


    @Test
    public void listUsersTest () {

        List<User> userInRepository = new ArrayList<>();
        User user = new User("FiveTester@test.com","567891","Five","Test","FiveTester@test.com","198765",30,"male","TW","Test:Case:05:listUsers","active","20210728","20210728");
        userInRepository.add(user);

        when(userServiceImpl.listUsers(userInRepository)).thenCallRealMethod();
        List<UserSummary> userSummary = userServiceImpl.listUsers(userInRepository);

        assertEquals("Five", userSummary.get(0).getFirstName());
        assertEquals("Test", userSummary.get(0).getLastName());
        assertEquals("FiveTester@test.com", userSummary.get(0).getEmail());
        assertEquals(30, userSummary.get(0).getAge());
        assertEquals(Arrays.asList("Test","Case","05","listUsers"), userSummary.get(0).getTags());
    }

    @Test
    public void getOneUserDetailsTest () {
        User userInRepository = new User("SixTester@test.com","678912","Six","Test","SixTester@test.com","219876",30,"male","TW","Test:Case:06:listOneUser","active","20210728","20210728");

        when(userServiceImpl.getOneUserDetails(userInRepository)).thenCallRealMethod();
        UserDetails userDetail = userServiceImpl.getOneUserDetails(userInRepository);

        assertEquals("678912", userDetail.getPassword());
        assertEquals("Six", userDetail.getFirstName());
        assertEquals("Test", userDetail.getLastName());
        assertEquals("SixTester@test.com", userDetail.getEmail());
        assertEquals("219876", userDetail.getContactNumber());
        assertEquals(30, userDetail.getAge());
        assertEquals("male", userDetail.getGender());
        assertEquals("TW", userDetail.getNationality());
        assertEquals(Arrays.asList("Test","Case","06","listOneUser"), userDetail.getTags());
    }
}
