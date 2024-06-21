package com.example.bookshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/review")
public class ReviewRestController {
    @ResponseBody
    @GetMapping("/list")
    public void ReviewRead(){
        //RestController
    }
    @ResponseBody
    @PostMapping("/register")
    public void ReviewRegister(){
        //RestController

    }
    @ResponseBody
    @PutMapping("/modify")
    public String ReviewModify(){
        //RestController
        return "/product/read";
    }
    @ResponseBody
    @DeleteMapping("/remove")
    public String ReviewRemove(){
        //RestController
        return "/product/read";
    }


}
