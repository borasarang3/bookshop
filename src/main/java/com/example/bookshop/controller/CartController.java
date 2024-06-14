package com.example.bookshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/cart")
public class CartController {

    @GetMapping("/list")
    public void cartList(){

    }

    @PostMapping("/plus")
    public void cartPlus(){
        //RestController

    }

    @PutMapping("/update/{cartItemId}")
    public String cartUpdate(){
        //RestController
        return "/cart/list";
    }

    @DeleteMapping("/remove/{cartItemId}")
    public String cartRemove(){
        //RestController
        return "/cart/list";
    }




}
