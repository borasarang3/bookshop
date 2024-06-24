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
@RequestMapping("/cart")
public class CartController {

    private final CategoryService categoryService;

    @GetMapping("/list")
    public void cartList(Model model){
        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
        model.addAttribute("categoryList", categoryDTOList);
    }
    @ResponseBody
    @PostMapping("/plus")
    public void cartPlus(){
        //RestController

    }
    @ResponseBody
    @PutMapping("/update/{cartItemId}")
    public String cartUpdate(){
        //RestController
        return "/cart/list";
    }
    @ResponseBody
    @DeleteMapping("/remove/{cartItemId}")
    public String cartRemove(){
        //RestController
        return "/cart/list";
    }




}
