package com.example.bookshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/login")
public class LoginController {

    @GetMapping("")
    public String loginGet(){
        return "/login";
    }

    @PostMapping("")
    public String loginPost(){
        return "/login";
    }

    @GetMapping("/findID")
    public void findIDGet(){

    }

    @PostMapping("/findID")
    public void findIDPost(){

    }

    @GetMapping("/findPW")
    public void findPWGet(){

    }

    @PostMapping("/findPW")
    public void findPWPost(){

    }





}
