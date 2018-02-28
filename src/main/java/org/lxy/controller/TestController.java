package org.lxy.controller;

import org.lxy.common.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping(value = "/test1")
    public ApiResult test1(@RequestParam int a, @RequestParam int b) {
        return ApiResult.success(a / b);
    }
}
