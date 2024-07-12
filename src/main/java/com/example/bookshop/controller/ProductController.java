package com.example.bookshop.controller;

import com.example.bookshop.constant.ItemSellStatus;
import com.example.bookshop.dto.*;
import com.example.bookshop.entity.Product;
import com.example.bookshop.service.CategoryService;
import com.example.bookshop.service.ImageService;
import com.example.bookshop.service.ProductService;
import com.example.bookshop.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.Optional;

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
    @GetMapping({"/list", "/list/{page}"})
    public String productList(Long cno,
                            ProductSearchDTO productSearchDTO,
                            @PathVariable("page")Optional<Integer> page,
                            Model model){

        log.info("선택된 카테고리 : " + cno );
        log.info("productSearchDTO : " + productSearchDTO);
        log.info("page : " + page);

        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
        model.addAttribute("categoryList", categoryDTOList);
        model.addAttribute("categoryNum", cno);

        // page 가지고 pageable 생성 // 값이 있으면 가져오고 없으면 1번부터 3개
        // 테스트하고 10개로 만들어두기
        Pageable pageable =
                PageRequest.of(page.isPresent() ? page.get() : 0, 10);

        if (cno == null){
            Page<MainProductDTO> products = productService.getProductImagPage(productSearchDTO, pageable);
            log.info(products);
            model.addAttribute("products", products);
            model.addAttribute("productSearchDTO", productSearchDTO);
            model.addAttribute("maxPage", 10);
            return "/product/list";

        } else {

            Page<MainProductDTO> products = productService.getMainProductCategoryPage(cno,productSearchDTO, pageable);
            log.info("있나요? : " + products);
            model.addAttribute("products", products);
            model.addAttribute("productSearchDTO", productSearchDTO);
            model.addAttribute("maxPage", 10);
            return "/product/list";

        }


    }

    @GetMapping("/read/{pno}")
    public String productRead(@PathVariable("pno") Long pno, Principal principal, Model model){
        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
        model.addAttribute("categoryList", categoryDTOList);

        ProductDTO productDTO = productService.productRead(pno);
        log.info(productDTO);
        model.addAttribute("productDTO", productDTO);

        if (principal != null){
            UserDTO userDTO = userService.read(principal.getName());
            model.addAttribute("userDTO", userDTO);
        }

        return "/product/read";



    }

    //상품 등록 진입
    @GetMapping("/register")
    public String productRegister(Principal principal,
                                ProductDTO productDTO,
                                RedirectAttributes redirectAttributes,
                                Model model){
        //post 접근 불가로 get 변경해둠
        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
        model.addAttribute("categoryList", categoryDTOList);

        if (principal == null){
            log.info("로그인시 이용가능한 서비스입니다.");
            redirectAttributes.addFlashAttribute("result", "로그인시 이용가능한 서비스입니다.");
            return "redirect:/login";
        }

        UserDTO userDTO = userService.read(principal.getName());

        productDTO.setSeller(userDTO.getUserName());
        productDTO.setItemSellStatus(ItemSellStatus.SELL);

        model.addAttribute("productDTO", productDTO);

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

        return "redirect:/";
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
                                    Model model,
                                    @RequestParam("imgFile")List<MultipartFile> imageFileList)
            throws Exception {

        log.info("입력받은 productDTO : " + productDTO);

        if (bindingResult.hasErrors()){
            log.info("상품 수정 오류 : " + bindingResult.hasErrors());
            redirectAttributes.addFlashAttribute("result", "상품 정보를 다시 확인해주세요.");
            //"/modify/{pno}"는 @PathVariable("pno")를 사용하는 방식
            //"/product/modify/" + productDTO.getPno()로 표현하면 위의 페이지와 같음
            return "redirect:/product/modify/" + productDTO.getPno();
        }

        if (imageFileList.get(0).isEmpty() && productDTO.getPno() == null){
            redirectAttributes.addFlashAttribute("result", "이미지를 업로드 해주세요.");
            return "redirect:/product/modify/" + productDTO.getPno();
        }

        log.info("getBytes : " + Arrays.toString(imageFileList.get(0).getBytes()));
        log.info("getContentType : " + imageFileList.get(0).getContentType());
        log.info("getOriginalFilename : " + imageFileList.get(0).getOriginalFilename());
        log.info("정상 값 : " + productDTO);

        productService.productModify(productDTO, imageFileList);

        redirectAttributes.addFlashAttribute("result", "상품 정보가 수정되었습니다.");

        return "redirect:/user/myproduct";
    }

    @PostMapping("/remove")
    public String productRemove(ProductDTO productDTO,
                                RedirectAttributes redirectAttributes){

        productService.productRemove(productDTO);

        redirectAttributes.addFlashAttribute("result", "상품이 삭제되었습니다.");

        return "redirect:/user/myproduct";
    }



}
