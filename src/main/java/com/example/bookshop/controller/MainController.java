package com.example.bookshop.controller;

import com.example.bookshop.dto.CategoryDTO;
import com.example.bookshop.dto.MainProductDTO;
import com.example.bookshop.dto.ProductSearchDTO;
import com.example.bookshop.service.CategoryService;
import com.example.bookshop.service.ImageService;
import com.example.bookshop.service.ProductService;
import com.example.bookshop.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Log4j2
public class MainController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final UserService userService;
    private final ImageService imageService;

    @GetMapping({"/", "/{page}"})
    public String main(ProductSearchDTO productSearchDTO,
                       @PathVariable("page") Optional<Integer> page,
                       Model model){

        log.info("productSearchDTO : " + productSearchDTO);
        log.info("page : " + page);

        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
        model.addAttribute("categoryList", categoryDTOList);

        // page 가지고 pageable 생성 // 값이 있으면 가져오고 없으면 뒷번호부터 4개
        Pageable pageable =
                PageRequest.of(page.isPresent() ? page.get() : 0, 4);

        Page<MainProductDTO> products = productService.getProductImagPageDesc(productSearchDTO, pageable);

        model.addAttribute("products", products);
        model.addAttribute("productSearchDTO", productSearchDTO);
        model.addAttribute("maxPage", 3);


        return "/main";

    }

//    @GetMapping("/{page}")
//    public @ResponseBody String mainRest(@RequestBody ProductSearchDTO productSearchDTO,
//                                         @PathVariable("page") Optional<Integer> page,
//                                         Model model){
//
//        log.info("productSearchDTO : " + productSearchDTO);
//        log.info("page : " + page);
//
//        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
//        model.addAttribute("categoryList", categoryDTOList);
//
//        // page 가지고 pageable 생성 // 값이 있으면 가져오고 없으면 뒷번호부터 4개
//        Pageable pageable =
//                PageRequest.of(page.isPresent() ? page.get() : 0, 4);
//
//        Page<MainProductDTO> products = productService.getProductImagPageDesc(productSearchDTO, pageable);
//
//        model.addAttribute("products", products);
//        model.addAttribute("productSearchDTO", productSearchDTO);
//        model.addAttribute("maxPage", 3);
//
//
//        return "/main";
//
//    }


}
