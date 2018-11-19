package com.wqs.qctwe.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:wqs
 * @date:2018/11/2
 * @desciption:
 */
@Api(description = "测试的api")
@RestController
public class testController {


    @ApiOperation(value = "2222")
    @GetMapping("/index")
    public String get(){
        return "test";
    }
}
