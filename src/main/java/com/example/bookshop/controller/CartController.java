package com.example.bookshop.controller;

import com.example.bookshop.dto.CartDetailDTO;
import com.example.bookshop.dto.CartItemDTO;
import com.example.bookshop.dto.CartOrderDTO;
import com.example.bookshop.dto.CategoryDTO;
import com.example.bookshop.service.CartService;
import com.example.bookshop.service.CategoryService;
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

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/cart")
public class CartController {

    private final CategoryService categoryService;
    private final CartService cartService;

    //장바구니 목록
    @GetMapping("/list")
    public String cartList(Principal principal, Model model){
        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
        model.addAttribute("categoryList", categoryDTOList);

        List<CartDetailDTO> cartDetailDTOList
                = cartService.getCartList(principal.getName());
        // userId을 파라미터로 넘긴다.

        model.addAttribute("cartDetailDTOList", cartDetailDTOList);

        return "/cart/list";

    }

    //장바구니 추가
    @PostMapping("/plus")
    public @ResponseBody ResponseEntity cartPlus(
            @RequestBody @Valid CartItemDTO cartItemDTO,
            BindingResult bindingResult, Principal principal){

        if (principal == null){
            return new ResponseEntity<String>("로그인시 이용 가능한 기능입니다.", HttpStatus.UNAUTHORIZED);
        }

        log.info("입력받은 장바구니 아이템 아이디 : " + cartItemDTO.getPno());
        log.info("입력받은 장바구니 아이템 수량 : " + cartItemDTO.getCount());

        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }//Valid 수행하며 조건에 부합하지 않을 경우 에러 필드와
            //에러메시지가 담긴다. dto에 선언해놓음

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String userId = principal.getName(); //로그인한 사람의 아이디
        Long cartItemId;

        // 서비스를 통해서 카트에 아이템을 담는다. addCart(cartItemDTO, email);
        // cartItemDTO에 pno로 아이템을 findbyId로 객체를 가져와서 참조하도록

        try {

            cartItemId = cartService.addCart(cartItemDTO, userId);

        } catch (Exception e) {
            return new ResponseEntity<String>
                    (e.getMessage(), HttpStatus.BAD_REQUEST);

        }



        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);

    }

    //장바구니 상품 변경
    @PutMapping("/update/{cartItemId}")
    public @ResponseBody ResponseEntity cartUpdate(
            @PathVariable("cartItemId") Long cartItemId,
            Long count, Principal principal){

        //url로 받은 파라미터 cartItemId와
        //쿼리스트링으로 받은 count를 이용하여
        // db에서 cartItem을 찾아서 수량을 변경 수행

        if (count <= 0){
            return new ResponseEntity<String>("최소 주문 수량은 1개입니다.", HttpStatus.BAD_REQUEST);
        } else if (cartService.validateCartItem(cartItemId, principal.getName())){
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }
        //위 장애물을 건넜다면 수량 변경
        cartService.updateCartItemCount(cartItemId, count);     //수량변경


        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);


    }

    //장바구니 상품 삭제
    @DeleteMapping("/remove/{cartItemId}")
    public @ResponseBody ResponseEntity cartRemove(
            @PathVariable("cartItemId") Long cartItemId,
            Principal principal){

        //System.out.println("넘어온 cartItemId : " + cartItemId);
        //넘어온 값이 확인되면 저 값을 이용해서
        //아까 확인한대로 service에서 id를 넘겨주고 그 아이디를 통해서
        // repository로 id를 넘겨서 delete(Long id) 메소드를 통해서 삭제
        // id를 넘겨서 entity를 찾아오고 예외처리한 후 entity를 가지고 delete(entity) 수행

        if (principal != null){
            System.out.println("로그인한 사람 : " + principal.getName());
        }

        if (cartService.validateCartItem(cartItemId, principal.getName())){
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        String cartItemNm = cartService.deleteCartItem(cartItemId);


//        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
        return new ResponseEntity<String>(cartItemNm, HttpStatus.OK);

    }

    //장바구니 > 주문 넘어가기
    @PostMapping("/cart/orders")
    public @ResponseBody ResponseEntity orderCartItem(
            @RequestBody CartOrderDTO cartOrderDTO, Principal principal) {

        log.info("cartOrderDTO : " + cartOrderDTO);

        List<CartOrderDTO> cartOrderDTOList = cartOrderDTO.getCartOrderDTOList();

        if (cartOrderDTOList == null || cartOrderDTOList.size() == 0) {
            //장바구니에서 주문할 카트아이템들을 체크해서 값을 보내지 않았다면
            return new ResponseEntity<String>("주문할 상품을 선택해주세요.", HttpStatus.FORBIDDEN);
        }

        for (CartOrderDTO cartOrderDTO1 : cartOrderDTOList) {
            if (cartService.validateCartItem(cartOrderDTO1.getCartItemId(), principal.getName())) {
                return new ResponseEntity<String>("주문 권한이 없습니다.",HttpStatus.FORBIDDEN);
            }
        }

        return new ResponseEntity<Long>
                (cartService.orderCartItem(cartOrderDTOList, principal.getName()), HttpStatus.OK);

    }




}
