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
@RequestMapping("/product")
public class ProductController {

    @GetMapping("/list")
    public void productList(){

    }

    @GetMapping("/read")
    public void productRead(){

    }

    @GetMapping("/register")
    public void productRegister(){
        //post 접근 불가로 get 변경해둠
    }

    @PostMapping("/register")
    public String productRegisterPost(){
        return "/product/register";
    }

    @PostMapping("/modify")
    public void productModify(){

    }

    @PostMapping("/modifypost")
    public String productModifyPost(){
        return "/product/modify";
    }

    @PostMapping("/remove")
    public String productRemove(){
        return "/product/modify";
    }



}
