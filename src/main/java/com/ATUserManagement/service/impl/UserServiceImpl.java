package com.ATUserManagement.service.impl;

import com.ATUserManagement.entity.*;
import com.ATUserManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public User addNewUser(AddUserRequest addUserRequest) {
        User userDetailGenerate = detailGenerate(addUserRequest);
        User userDetailGuess = detailGuess(addUserRequest.getFirstName());

        return new User(userDetailGenerate.getUsername(),
                addUserRequest.getPassword(),
                addUserRequest.getFirstName(),
                addUserRequest.getLastName(),
                addUserRequest.getEmail(),
                addUserRequest.getContactNumber(),
                userDetailGuess.getAge(),
                userDetailGuess.getGender(),
                userDetailGuess.getNationality(),
                userDetailGenerate.getTags(),
                userDetailGenerate.getStatus(),
                userDetailGenerate.getCreated(),
                userDetailGenerate.getUpdated());
    }

    @Override
    public User updateOneUser(User userToUpdate, AddUserRequest addUserRequest) {

        if (userToUpdate.getFirstName() != addUserRequest.getFirstName() && addUserRequest.getFirstName() != null) {
            User userDetailGuess = detailGuess(addUserRequest.getFirstName());
            userToUpdate.setFirstName(addUserRequest.getFirstName());
            userToUpdate.setAge(userDetailGuess.getAge());
            userToUpdate.setGender(userDetailGuess.getGender());
            userToUpdate.setNationality(userDetailGuess.getNationality());
        }

        if (addUserRequest.getLastName() != null) { userToUpdate.setLastName(addUserRequest.getLastName());}
        if (addUserRequest.getPassword() != null) { userToUpdate.setPassword(addUserRequest.getPassword());}
        if (addUserRequest.getContactNumber() != null) { userToUpdate.setContactNumber(addUserRequest.getContactNumber());}
//        if (addUserRequest.getAge() != 0) { userToUpdate.setAge(userUpdateDetail.getAge());}
//        if (addUserRequest.getGender() != null) { userToUpdate.setGender(userUpdateDetail.getGender());}
//        if (addUserRequest.getNationality() != null) { userToUpdate.setNationality(userUpdateDetail.getNationality());}

        User userDetailGenerate = detailGenerate(addUserRequest);
        if (addUserRequest.getTags() != null) { userToUpdate.setTags(userDetailGenerate.getTags());}
        userToUpdate.setUpdated(userDetailGenerate.getUpdated());

        return userToUpdate;
    }

    public static String getStringDate() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(new Date());
    }

    public User detailGenerate (AddUserRequest addUserRequest) {

        User userDetailGenerate = new User();

        userDetailGenerate.setUsername(addUserRequest.getEmail());

        String tags = addUserRequest.getTags().stream().collect(Collectors.joining(":"));
        userDetailGenerate.setTags(tags);

        userDetailGenerate.setStatus("active");

        userDetailGenerate.setCreated(getStringDate());
        userDetailGenerate.setUpdated(getStringDate());

        return userDetailGenerate;
    }

    public User detailGuess (String firstName) {

        User userDetailGuess = new User();
        UserDetailProcess userResponse = new UserDetailProcess();

        userResponse = restTemplate.getForObject("https://api.agify.io/?name={?}", UserDetailProcess.class,firstName);
        userDetailGuess.setAge(userResponse.getAge());

        userResponse = restTemplate.getForObject("https://api.genderize.io?name={name}", UserDetailProcess.class, firstName);
        userDetailGuess.setGender(userResponse.getGender());

        userResponse = restTemplate.getForObject("https://api.nationalize.io?name={name}", UserDetailProcess.class, firstName);
        if (!userResponse.getCountry().isEmpty()) {
            userDetailGuess.setNationality((String)userResponse.getCountry().get(0).get("country_id"));
        } else {
            userDetailGuess.setNationality(null);
        }

        return userDetailGuess;
    }

    public List<UserSummary> listUsers (List<User> userInRepository) {
        List<UserSummary> userSummary = new ArrayList<>();
        for (User user : userInRepository) {
            UserSummary userInList = new UserSummary();
            userInList.setFirstName(user.getFirstName());
            userInList.setLastName(user.getLastName());
            userInList.setEmail(user.getEmail());
            userInList.setAge(user.getAge());
            userInList.setContactNumber(user.getContactNumber());
            userInList.setTags(Arrays.asList(user.getTags().split(":")));
            userSummary.add(userInList);
        }
        return userSummary;
    }

    public UserDetails getOneUserDetails (User userInRepository) {
        UserDetails userDetail = new UserDetails();
        userDetail.setPassword(userInRepository.getPassword());
        userDetail.setFirstName(userInRepository.getFirstName());
        userDetail.setLastName(userInRepository.getLastName());
        userDetail.setEmail(userInRepository.getEmail());
        userDetail.setContactNumber(userInRepository.getContactNumber());
        userDetail.setAge(userInRepository.getAge());
        userDetail.setGender(userInRepository.getGender());
        userDetail.setNationality(userInRepository.getNationality());
        userDetail.setTags(Arrays.asList(userInRepository.getTags().split(":")));

        return userDetail;
    }
}
