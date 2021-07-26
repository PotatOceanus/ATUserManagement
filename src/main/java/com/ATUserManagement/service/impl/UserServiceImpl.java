package com.ATUserManagement.service.impl;

import com.ATUserManagement.entity.User;
import com.ATUserManagement.entity.User_detail_process;
import com.ATUserManagement.entity.User_details;
import com.ATUserManagement.entity.User_list;
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
    public User addNewUser(User_detail_process user_push) {
        User user_detail_generate = detail_generate(user_push);
        User user_detail_guess = detail_guess(user_push.getFirstName());

        return new User(user_detail_generate.getUsername(),
                user_push.getPassword(),
                user_push.getFirstName(),
                user_push.getLastName(),
                user_push.getEmail(),
                user_push.getContactNumber(),
                user_detail_guess.getAge(),
                user_detail_guess.getGender(),
                user_detail_guess.getNationality(),
                user_detail_generate.getTags(),
                user_detail_generate.getStatus(),
                user_detail_generate.getCreated(),
                user_detail_generate.getUpdated());
    }

    @Override
    public User updateOneUser(User user_to_update, User_detail_process user_push) {

        if (user_to_update.getFirstName() != user_push.getFirstName() && user_push.getFirstName() != null) {
            User user_detail_guess = detail_guess(user_push.getFirstName());
            user_to_update.setFirstName(user_push.getFirstName());
            user_to_update.setAge(user_detail_guess.getAge());
            user_to_update.setGender(user_detail_guess.getGender());
            user_to_update.setNationality(user_detail_guess.getNationality());
        }

        if (user_push.getLastName() != null) { user_to_update.setLastName(user_push.getLastName());}
        if (user_push.getPassword() != null) { user_to_update.setPassword(user_push.getPassword());}
        if (user_push.getContactNumber() != null) { user_to_update.setContactNumber(user_push.getContactNumber());}
        if (user_push.getAge() != 0) { user_to_update.setAge(user_push.getAge());}
        if (user_push.getGender() != null) { user_to_update.setGender(user_push.getGender());}
        if (user_push.getNationality() != null) { user_to_update.setNationality(user_push.getNationality());}

        User user_detail_generate = detail_generate(user_push);
        if (user_push.getTags() != null) { user_to_update.setTags(user_detail_generate.getTags());}
        user_to_update.setUpdated(user_detail_generate.getUpdated());

        return user_to_update;
    }

    public static String getStringDate() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(new Date());
    }

    public User detail_generate (User_detail_process user_detailprocess) {
        User user_detail_generate = new User();

        String username = user_detailprocess.getEmail();
        user_detail_generate.setUsername(username);

        String tags = user_detailprocess.getTags().stream().collect(Collectors.joining(":"));
        user_detail_generate.setTags(tags);

        user_detail_generate.setStatus("active");

        user_detail_generate.setCreated(getStringDate());
        user_detail_generate.setUpdated(getStringDate());

        return user_detail_generate;
    }

    public User detail_guess (String firstName) {
        User user_detail_guess = new User();
        User_detail_process user_res = new User_detail_process();

        user_res = restTemplate.getForObject("https://api.agify.io/?name={?}", User_detail_process.class,firstName);
        user_detail_guess.setAge(user_res.getAge());

        user_res = restTemplate.getForObject("https://api.genderize.io?name={name}", User_detail_process.class, firstName);
        user_detail_guess.setGender(user_res.getGender());

        user_res = restTemplate.getForObject("https://api.nationalize.io?name={name}", User_detail_process.class, firstName);
        if (!user_res.getCountry().isEmpty()) {
            user_detail_guess.setNationality((String)user_res.getCountry().get(0).get("country_id"));
        } else {
            user_detail_guess.setNationality(null);
        }

        return user_detail_guess;
    }

    public List<User_list> listUsers (List<User> users) {
        List<User_list> user_lists = new ArrayList<>();
        for (User user : users) {
            User_list user_list = new User_list();
            user_list.setFirstName(user.getFirstName());
            user_list.setLastName(user.getLastName());
            user_list.setEmail(user.getEmail());
            user_list.setAge(user.getAge());
            user_list.setTags(Arrays.asList(user.getTags().split(":")));
            user_lists.add(user_list);
        }
        return user_lists;
    }

    public User_details getOneUserDetails (User user) {
        User_details user_details = new User_details();
        user_details.setPassword(user.getPassword());
        user_details.setFirstName(user.getFirstName());
        user_details.setLastName(user.getLastName());
        user_details.setEmail(user.getEmail());
        user_details.setContactNumber(user.getContactNumber());
        user_details.setAge(user.getAge());
        user_details.setGender(user.getGender());
        user_details.setNationality(user.getNationality());
        user_details.setTags(Arrays.asList(user.getTags().split(":")));

        return user_details;
    }
}
