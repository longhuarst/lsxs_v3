package com.lsxs.controller;

import com.lsxs.entity.Result;
import com.lsxs.entity.StatusCode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    @RequestMapping("test")
    public Result test() {
        return new Result(false, StatusCode.OK, "test");
    }

}
