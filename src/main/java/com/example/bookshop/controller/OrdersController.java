package com.example.bookshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/orders")
public class OrdersController {

    @GetMapping("")
    public void orders(){

    }
    @ResponseBody
    @PutMapping("/modify")
    public void ordersModify(){
        //RestController
    }
    @ResponseBody
    @DeleteMapping("/remove")
    public String ordersRemove(){
        //RestController
        return "/orders";
    }
    @ResponseBody
    @PostMapping("/buy")
    public String ordersBuy(){
        //RestController
        return "/orders";
    }

}
