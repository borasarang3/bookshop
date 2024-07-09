package com.example.bookshop.controller;

import com.example.bookshop.dto.*;
import com.example.bookshop.entity.CartItem;
import com.example.bookshop.service.CartService;
import com.example.bookshop.service.CategoryService;
import com.example.bookshop.service.OrdersService;
import com.example.bookshop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/orders")
public class OrdersController {

    private final CategoryService categoryService;
    private final OrdersService ordersService;
    private final CartService cartService;
    private final ProductService productService;

    @GetMapping("")
    public void orders(Principal principal, Model model){
        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
        model.addAttribute("categoryList", categoryDTOList);

        List<CartDetailDTO> cartDetailDTOList
                = cartService.getCartList(principal.getName());
        // userId을 파라미터로 넘긴다.

        model.addAttribute("cartDetailDTOList", cartDetailDTOList);
    }

    @PostMapping("")
    public String orders(Long[] cartChkBox,
                       RedirectAttributes redirectAttributes,
                       Principal principal, Model model){

        log.info("카트에서 가져온 아이템 카트아이템 번호 : " + Arrays.toString(cartChkBox));


       if (cartChkBox == null || cartChkBox.length == 0) {
            //장바구니에서 주문할 카트아이템들을 체크해서 값을 보내지 않았다면
            redirectAttributes.addFlashAttribute("error", "주문할 상품을 선택해주세요.");
            return "redirect:/cart/list";

        }

        for (Long cartItemId : cartChkBox) {
            if (cartService.validateCartItem(cartItemId, principal.getName())) {
                redirectAttributes.addFlashAttribute("error", "주문 가능한 장바구니가 아닙니다.");
                return  "redirect:/cart/list";
            }
        }

        List<CartDetailDTO> cartDetailDTOList = cartService.findOrder(cartChkBox);

        cartDetailDTOList.forEach(cartDetailDTO -> log.info( "값 : " + cartDetailDTO));

        model.addAttribute("cartDetailDTOList", cartDetailDTOList);

        return "/orders";
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

    @PostMapping("/buy")
    public  @ResponseBody ResponseEntity ordersBuy(
            @RequestBody CartOrderDTO cartOrderDTO,
            Principal principal, Model model){

        log.info(cartOrderDTO);

        log.info("cartOrderDTO : " + cartOrderDTO);

        List<CartOrderDTO> cartOrderDTOList = cartOrderDTO.getCartOrderDTOList();

        return new ResponseEntity<Long>
                (cartService.orderCartItem(cartOrderDTOList, principal.getName()), HttpStatus.OK);



//        //principal : 현재 로그인된 사람의 정보
//        if (principal.getName() == null){
//            return new ResponseEntity<String>("로그인시 이용 가능한 서비스입니다.".toString(), HttpStatus.BAD_REQUEST);
//        }
//        log.info("로그인 정보" + principal);
//        //여기서 이름은 실제 이름이 아닌 시큐리티의 username 즉 id를 뜻함
//        //로그인을 하지 않았다면 에러 차후에 예외처리와 로그인처리 해야 함
//
//        if (principal == null) {
//            return new ResponseEntity<String>("로그인시 이용 가능한 서비스입니다.", HttpStatus.UNAUTHORIZED);
//        }
//        log.info("로그인한 사람 이름" + principal.getName());
//        log.info(ordersDTO);
//
//        //유효성검사
//        if (bindingResult.hasErrors()){
//            StringBuilder stringBuilder = new StringBuilder();
//            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//            for (FieldError fieldError : fieldErrors) {
//                stringBuilder.append(fieldError.getDefaultMessage());
//            }
//
//            return new ResponseEntity<String>(stringBuilder.toString(), HttpStatus.BAD_REQUEST);
//        }
//
//        String userId = principal.getName();
//        Long ordersId;
//
//        try {
//            // 서비스에서 주문을 내고 db에 저장된 값을 돌려 받는다. 그중에 id
//            // orderId = 리턴
//            ordersId = ordersService.order(ordersDTO, userId);
//            log.info("주문한 ordersId : " + ordersId);
//        } catch (Exception e) {
//            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//
//        // 입력받은 파라미터가 정상적이라면
//        // 이후 service를 통해서 저장, 수정, 삭제
//        // 읽기 등을 동일하게 수행하면 됨
//
//
//        return new ResponseEntity<Long>(ordersId, HttpStatus.OK);

    }


}
