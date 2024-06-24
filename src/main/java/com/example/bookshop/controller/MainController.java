package com.example.bookshop.controller;

import com.example.bookshop.dto.CategoryDTO;
import com.example.bookshop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class MainController {

    private final CategoryService categoryService;

    @GetMapping({"/", "/{page}"})
    public String main(Model model){

        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
        model.addAttribute("categoryList", categoryDTOList);

        return "/main";

    }


}
