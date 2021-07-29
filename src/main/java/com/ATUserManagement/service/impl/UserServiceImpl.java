package com.ATUserManagement.service.impl;

import com.ATUserManagement.entity.User;
import com.ATUserManagement.entity.UserDetailProcess;
import com.ATUserManagement.entity.OneUserDetail;
import com.ATUserManagement.entity.AllUserList;
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
    public User addNewUser(UserDetailProcess userCreateDetail) {
        User userDetailGenerate = detailGenerate(userCreateDetail);
        User userDetailGuess = detailGuess(userCreateDetail.getFirstName());

        return new User(userDetailGenerate.getUsername(),
                userCreateDetail.getPassword(),
                userCreateDetail.getFirstName(),
                userCreateDetail.getLastName(),
                userCreateDetail.getEmail(),
                userCreateDetail.getContactNumber(),
                userDetailGuess.getAge(),
                userDetailGuess.getGender(),
                userDetailGuess.getNationality(),
                userDetailGenerate.getTags(),
                userDetailGenerate.getStatus(),
                userDetailGenerate.getCreated(),
                userDetailGenerate.getUpdated());
    }

    @Override
    public User updateOneUser(User userToUpdate, UserDetailProcess userUpdateDetail) {

        if (userToUpdate.getFirstName() != userUpdateDetail.getFirstName() && userUpdateDetail.getFirstName() != null) {
            User userDetailGuess = detailGuess(userUpdateDetail.getFirstName());
            userToUpdate.setFirstName(userUpdateDetail.getFirstName());
            userToUpdate.setAge(userDetailGuess.getAge());
            userToUpdate.setGender(userDetailGuess.getGender());
            userToUpdate.setNationality(userDetailGuess.getNationality());
        }

        if (userUpdateDetail.getLastName() != null) { userToUpdate.setLastName(userUpdateDetail.getLastName());}
        if (userUpdateDetail.getPassword() != null) { userToUpdate.setPassword(userUpdateDetail.getPassword());}
        if (userUpdateDetail.getContactNumber() != null) { userToUpdate.setContactNumber(userUpdateDetail.getContactNumber());}
        if (userUpdateDetail.getAge() != 0) { userToUpdate.setAge(userUpdateDetail.getAge());}
        if (userUpdateDetail.getGender() != null) { userToUpdate.setGender(userUpdateDetail.getGender());}
        if (userUpdateDetail.getNationality() != null) { userToUpdate.setNationality(userUpdateDetail.getNationality());}

        User userDetailGenerate = detailGenerate(userUpdateDetail);
        if (userUpdateDetail.getTags() != null) { userToUpdate.setTags(userDetailGenerate.getTags());}
        userToUpdate.setUpdated(userDetailGenerate.getUpdated());

        return userToUpdate;
    }

    public static String getStringDate() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(new Date());
    }

    public User detailGenerate (UserDetailProcess userDetailPost) {

        User userDetailGenerate = new User();

        userDetailGenerate.setUsername(userDetailPost.getEmail());

        String tags = userDetailPost.getTags().stream().collect(Collectors.joining(":"));
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

    public List<AllUserList> listUsers (List<User> userInRepository) {
        List<AllUserList> allUserList = new ArrayList<>();
        for (User user : userInRepository) {
            AllUserList userInList = new AllUserList();
            userInList.setFirstName(user.getFirstName());
            userInList.setLastName(user.getLastName());
            userInList.setEmail(user.getEmail());
            userInList.setAge(user.getAge());
            userInList.setContactNumber(user.getContactNumber());
            userInList.setTags(Arrays.asList(user.getTags().split(":")));
            allUserList.add(userInList);
        }
        return allUserList;
    }

    public OneUserDetail getOneUserDetails (User userInRepository) {
        OneUserDetail userDetail = new OneUserDetail();
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
