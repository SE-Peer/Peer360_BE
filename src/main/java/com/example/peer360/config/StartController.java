package com.example.peer360.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class StartController {


    /**
     * 초기화면, 연결 확인을 위한 용도
     * [GET]
     * @return index
     */
    @GetMapping("")
    public String index() {
        return "index";
    }
}
