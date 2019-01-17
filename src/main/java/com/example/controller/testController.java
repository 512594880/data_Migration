package com.example.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by wangxi on 2019/1/17.
 */
@RestController
public class testController {

    @PostMapping("/test")
    public List<Object> test(){
        return null;
    }
}
