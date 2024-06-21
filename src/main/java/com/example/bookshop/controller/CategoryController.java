package com.example.bookshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/category")
public class CategoryController {

    @GetMapping("")
    public void category(){

    }

    @ResponseBody
    @PostMapping("/register")
    public String categoryRegister(){
        return "/category";
    }
    @ResponseBody
    @PutMapping("/modify")
    public String categoryModify(){
        //RestController
        return "/category";
    }
    @ResponseBody
    @GetMapping("/product")
    public String categoryProduct(){
        //RestController
        return "/category";
    }
    @ResponseBody
    @DeleteMapping("/remove")
    public String categoryRemove(){
        return "/category";
    }


}
