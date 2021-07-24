package com.ATUserManagement.service.impl;

import com.ATUserManagement.entity.User;
import com.ATUserManagement.entity.User_detail_process;
import com.ATUserManagement.service.AddUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
public class AddUserServiceImpl implements AddUserService {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public User addNewUser(User_detail_process user_push) {
        User user_detail_generate = detail_generate(user_push);
        User user_detail_guess = detail_guess(user_push.getFirstName());

        User user = new User(user_detail_generate.getUsername(),
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

        return user;
    }

    public static String getStringDate() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String timestamp = df.format(new Date());
        return timestamp;
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
}
