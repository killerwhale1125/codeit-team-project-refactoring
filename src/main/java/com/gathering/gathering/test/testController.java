package com.gathering.gathering.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {

    @RequestMapping(value = "/test" , method = RequestMethod.GET)
    public String test() {
        return "test";
    }
}
