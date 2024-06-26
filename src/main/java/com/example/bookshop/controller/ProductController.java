package com.example.bookshop.controller;

import com.example.bookshop.constant.ItemSellStatus;
import com.example.bookshop.dto.CategoryDTO;
import com.example.bookshop.dto.ImageDTO;
import com.example.bookshop.dto.ProductDTO;
import com.example.bookshop.dto.UserDTO;
import com.example.bookshop.service.CategoryService;
import com.example.bookshop.service.ImageService;
import com.example.bookshop.service.ProductService;
import com.example.bookshop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ImageService imageService;

    //상품 목록
    @GetMapping("/list")
    public void productList(Model model){
        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
        model.addAttribute("categoryList", categoryDTOList);

        model.addAttribute("productDTOList", productService.list());

    }

    @GetMapping("/read/{pno}")
    public String productRead(@PathVariable("pno") Long pno, Model model){
        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
        model.addAttribute("categoryList", categoryDTOList);

        ProductDTO productDTO = productService.productRead(pno);
        log.info(productDTO);
        model.addAttribute("productDTO", productDTO);

        return "/product/read";



    }

    //상품 등록 진입
    @GetMapping("/register")
    public String productRegister(Principal principal,
                                ProductDTO productDTO,
                                RedirectAttributes redirectAttributes,
                                Model model){
        //post 접근 불가로 get 변경해둠

        if (principal.getName() == null){
            log.info("로그인시 이용가능한 서비스입니다.");
            redirectAttributes.addFlashAttribute("result", "로그인시 이용가능한 서비스입니다.");
            return "redirect:/login";
        }

        UserDTO userDTO = userService.read(principal.getName());

        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();

        productDTO.setSeller(userDTO.getUserName());
        productDTO.setItemSellStatus(ItemSellStatus.SELL);

        model.addAttribute("productDTO", productDTO);
        model.addAttribute("categoryList", categoryDTOList);




        return "/product/register";
    }
    //상품 등록
    @PostMapping("/register")
    public String productRegisterPost(@Valid ProductDTO productDTO,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes,
                                      Model model,
                                      @RequestParam("imgFile")List<MultipartFile> imageFileList)
            throws IOException {

        //기본형을 만들고나서 파일 업로드 및 페이징처리 구현

        log.info("컨트롤러에서 받은 값 : " + productDTO);

        if (bindingResult.hasErrors()) {
            log.info("상품 등록 에러 발생 : " + productDTO);
            redirectAttributes.addFlashAttribute("result", "상품 등록에 실패했습니다. 내용을 다시 한번 확인해주세요.");
            return "redirect:/product/register";
        }

        if (imageFileList.get(0).isEmpty() && productDTO.getPno() == null){
            redirectAttributes.addFlashAttribute("result", "이미지를 업로드 해주세요.");
            return "redirect:/product/register";
        }

        log.info("getBytes : " + Arrays.toString(imageFileList.get(0).getBytes()));
        log.info("getContentType : " + imageFileList.get(0).getContentType());
        log.info("getOriginalFilename : " + imageFileList.get(0).getOriginalFilename());
        log.info("정상 값 : " + productDTO);


        productService.register(productDTO, imageFileList);

        redirectAttributes.addFlashAttribute("result", "상품 등록이 완료되었습니다.");

        return "redirect:/main";
    }

    @GetMapping("/modify/{pno}")
    public String productModify(@PathVariable("pno") Long pno, Model model){
        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
        model.addAttribute("categoryList", categoryDTOList);

        ProductDTO productDTO = productService.productRead(pno);
        log.info(productDTO);
        model.addAttribute("productDTO", productDTO);

        return "/product/modify";

    }

    @PostMapping("/modifypost")
    public String productModifyPost(@Valid ProductDTO productDTO,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes,
                                    Model model){

        log.info("입력받은 productDTO : " + productDTO);

        if (bindingResult.hasErrors()){
            log.info("상품 수정 오류 : " + bindingResult.hasErrors());
            redirectAttributes.addFlashAttribute("result", "상품 정보를 다시 확인해주세요.");
            //"/modify/{pno}"는 @PathVariable("pno")를 사용하는 방식
            //"/product/modify/" + productDTO.getPno()로 표현하면 위의 페이지와 같음
            return "redirect:/product/modify/" + productDTO.getPno();
        }

        productService.productModify(productDTO);

        redirectAttributes.addFlashAttribute("result", "상품 정보가 수정되었습니다.");

        return "redirect:/user/product";
    }

    @PostMapping("/remove")
    public String productRemove(ProductDTO productDTO,
                                RedirectAttributes redirectAttributes){

        productService.productRemove(productDTO);

        redirectAttributes.addFlashAttribute("result", "상품이 삭제되었습니다.");

        return "redirect:/user/product";
    }



}
