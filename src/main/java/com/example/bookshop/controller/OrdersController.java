package com.example.bookshop.controller;

import com.example.bookshop.dto.CategoryDTO;
import com.example.bookshop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/orders")
public class OrdersController {

    private final CategoryService categoryService;

    @GetMapping("")
    public void orders(Model model){
        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
        model.addAttribute("categoryList", categoryDTOList);
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
