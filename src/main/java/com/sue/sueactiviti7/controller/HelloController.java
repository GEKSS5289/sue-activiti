package com.sue.sueactiviti7.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sue
 * @date 2020/9/13 16:03
 */

@RestController
public class HelloController {
    @RequestMapping(value = "hello",method = RequestMethod.GET)
    public String hello(){
        return "Activiti7欢迎你";
    }
}
