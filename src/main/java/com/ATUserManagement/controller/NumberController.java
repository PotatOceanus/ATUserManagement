package com.ATUserManagement.controller;

import com.ATUserManagement.service.impl.NumberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@RestController
@RequestMapping("/api")
public class NumberController {

    @Autowired
    private NumberServiceImpl numberServiceImpl;

    @GetMapping("/calculation")
    @ResponseBody
    public HashMap calculate996(@RequestParam int max, @RequestParam int divisor, @RequestParam int limit) {

        return numberServiceImpl.calculator996(max, divisor, limit);
    }

}
