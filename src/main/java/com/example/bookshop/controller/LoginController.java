package com.example.bookshop.controller;

import com.example.bookshop.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @GetMapping("/error")
    public String loginError(Model model){


        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요" );
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
