package com.example.bookshop.controller;

import com.example.bookshop.constant.ItemSellStatus;
import com.example.bookshop.dto.CategoryDTO;
import com.example.bookshop.dto.ProductDTO;
import com.example.bookshop.dto.UserDTO;
import com.example.bookshop.service.CategoryService;
import com.example.bookshop.service.ProductService;
import com.example.bookshop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final CategoryService categoryService;

    @GetMapping("/list")
    public void productList(){

    }

    @GetMapping("/read")
    public void productRead(){

    }

    @GetMapping("/register")
    public void productRegister(Principal principal,
                                ProductDTO productDTO,
                                Model model){
        //post 접근 불가로 get 변경해둠

        UserDTO userDTO = userService.read(principal.getName());

        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();

        productDTO.setSeller(userDTO.getUserName());
        productDTO.setItemSellStatus(ItemSellStatus.SELL);

        model.addAttribute("productDTO", productDTO);
        model.addAttribute("categoryList", categoryDTOList);

    }
    //상품 등록
    @PostMapping("/register")
    public String productRegisterPost(@Valid ProductDTO productDTO,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes,
                                      Model model){

        //기본형을 만들고나서 파일 업로드 및 페이징처리 구현

        log.info("컨트롤러에서 받은 값 : " + productDTO);

        if (bindingResult.hasErrors()) {
            log.info("상품 등록 에러 발생 : " + productDTO);
            redirectAttributes.addFlashAttribute("result", bindingResult.hasErrors());
            return "/product/register";
        }

        productService.register(productDTO);

        redirectAttributes.addFlashAttribute("result", "상품 등록이 완료되었습니다.");

        return "/main";
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
