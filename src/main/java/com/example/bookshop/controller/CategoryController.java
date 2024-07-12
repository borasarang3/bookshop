package com.example.bookshop.controller;

import com.example.bookshop.dto.CategoryDTO;
import com.example.bookshop.dto.MainProductDTO;
import com.example.bookshop.dto.ProductSearchDTO;
import com.example.bookshop.dto.UserDTO;
import com.example.bookshop.service.CategoryService;
import com.example.bookshop.service.ProductService;
import com.example.bookshop.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping("")
    public String category(Model model, Principal principal,
                           ProductSearchDTO productSearchDTO, Pageable pageable,
                           RedirectAttributes redirectAttributes){

        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
        model.addAttribute("categoryList", categoryDTOList);

        if (principal.getName() == null){
            log.info("로그인시 이용가능한 서비스입니다.");
            redirectAttributes.addFlashAttribute("result", "로그인시 이용가능한 서비스입니다.");
            return "redirect:/login";
        }

        log.info("현재 로그인 회원 이름 : " + principal.getName());

        UserDTO loginUser = userService.read(principal.getName());

        if ( loginUser.getRole().name() == "ADMIN" ){

            model.addAttribute("categoryDTOList", categoryService.allCategoryList() );
            model.addAttribute("products",
                    productService.getProductImagPageDesc(productSearchDTO, pageable));
            model.addAttribute("maxPage", 10);
            return "/category";

        } else {

            redirectAttributes.addFlashAttribute("result", "열람 권한이 없습니다.");

            return "redirect:/";

        }


    }

    @ResponseBody
    @PostMapping("/register")
    public String categoryRegister(String cname){

        log.info("받은 카테고리 이름 : " + cname);

        CategoryDTO categoryDTO = categoryService.register(cname);

        if (categoryDTO == null) {
            return "a";
        }

        return "b";
    }

    @ResponseBody
    @PutMapping("/modify")
    public String categoryModify(String cname, String cname1){

        log.info("수정하려는 카테고리 이름 : " + cname1);
        log.info("받은 카테고리 이름 : " + cname);

        CategoryDTO categoryDTO = categoryService.modify(cname, cname1);

        if (categoryDTO == null) {
            return "a";
        }

        return "b";
    }

    @ResponseBody
    @GetMapping("/product")
    public String categoryProduct(Long cno, ProductSearchDTO productSearchDTO,
                                  @PathVariable("page") Optional<Integer> page,
                                  Model model){
        //RestController

        Pageable pageable =
                PageRequest.of(page.isPresent() ? page.get() : 0, 10);

        Page<MainProductDTO> products = productService.getMainProductCategoryPage(cno, productSearchDTO, pageable);

        model.addAttribute("products", products);
        model.addAttribute("productSearchDTO", productSearchDTO);
        model.addAttribute("maxPage", 10);

        return "/category";
    }

    @ResponseBody
    @DeleteMapping("/remove")
    public void categoryRemove(String cname){

        log.info("받은 카테고리 이름 : " + cname);

        categoryService.remove(cname);
    }


}
