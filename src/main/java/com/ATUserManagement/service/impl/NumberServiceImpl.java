package com.ATUserManagement.service.impl;

import com.ATUserManagement.service.NumberService;
import com.ATUserManagement.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class NumberServiceImpl implements NumberService {

    @Override
    public HashMap calculator996(int max, int divisor, int limit) {

        List<String> numbers = new ArrayList<>();
        HashMap<String, List> result = new HashMap<>();
        int start = max - max % divisor + 2;
        IntStream.range(0, limit)
                .forEach(i -> numbers.add("I am " + String.valueOf(start - divisor * i)));
        result.put("data", numbers);
        return result;
    }
}
