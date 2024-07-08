package com.example.bookshop.service;

import com.example.bookshop.dto.CartDetailDTO;
import com.example.bookshop.dto.CartItemDTO;
import com.example.bookshop.dto.CartOrderDTO;
import com.example.bookshop.dto.OrdersDTO;
import com.example.bookshop.entity.Cart;
import com.example.bookshop.entity.CartItem;
import com.example.bookshop.entity.Product;
import com.example.bookshop.entity.UserMember;
import com.example.bookshop.repository.CartItemRepository;
import com.example.bookshop.repository.CartRepository;
import com.example.bookshop.repository.ProductRepository;
import com.example.bookshop.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.module.FindException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ProductRepository productRepository; //장바구니에 넣을 아이템을 찾기 위하여
    private final UserRepository userRepository; //장바구니를 만들 회원
    private final CartRepository cartRepository; //장바구니 저장, 수정 등
    private final CartItemRepository cartItemRepository; //장바구니에 담을 카트 아이템
    private final OrdersService ordersService;

    //카트 목록 가져오기
    @Transactional(readOnly = true)
    public List<CartDetailDTO> getCartList(String userId){

        List<CartDetailDTO> cartDetailDTOList = new ArrayList<>();

        UserMember userMember = userRepository.findByUserId(userId);

        Cart cart = cartRepository.findByUserMemberUserId(userMember.getUserId());

        if (cart == null) {
            return cartDetailDTOList;
        }

        cartDetailDTOList = cartItemRepository.findCartDetail(cart.getCartId());

        return cartDetailDTOList;

    }

    //장바구니 추가
    public Long addCart(CartItemDTO cartItemDTO, String userId){
        //컨트롤러에서 입력받은 cartItemDTO의 id를 통해 상품을 찾습니다.
        Product product = productRepository.findById(cartItemDTO.getPno())
                .orElseThrow(EntityNotFoundException::new);

        // userId cart를 찾습니다.
        UserMember userMember = userRepository.findByUserId(userId);

        Cart cart = cartRepository.findByUserMemberUserId(userMember.getUserId());
        //회원으로 찾은 cart가 없다면
        if (cart == null) {
            cart = Cart.createCart(userMember);
            cartRepository.save(cart);
        }

        //찾은 상품을 cartItem으로 변환합니다.
        //카트에 이미 같은 상품이 있는지를 알고 싶어서 카트 아이템을 찾아봅니다.
        CartItem savedCartItem
                = cartItemRepository.findByCart_CartIdAndProductPno(cart.getCartId(), product.getPno());

        if (savedCartItem != null){
            //장바구니에 이미 같은 종류의 상품이 있다면 수량을 더해주고
            // 더티체킹을 통해서 트랜잭션이 끝날 때 update 실행
            savedCartItem.addCount(cartItemDTO.getCount());
            return savedCartItem.getCartItemId();
        } else {
            CartItem cartItem
                    = CartItem.CreateCartItem(cart, product, cartItemDTO.getCount());

            cartItemRepository.save(cartItem);
            return cartItem.getCartItemId();

        }

        // 변환된 X , 직접 카트 아이디와 아이템id로 찾거나
        // 직접 생성해서 cartItem을 cart에 담습니다.

    }

    //현재 장바구니가 내꺼인지 확인하는 메소드
    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String userId){

        CartItem cartItem = cartItemRepository
                .findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        System.out.println("userId : " + userId );
        System.out.println("아이템 제작자 : " + cartItem.getCreateBy());

        if (cartItem.getCreateBy().equals(userId)){
            // == 이면 객체를 비교하고, .equals는 값을 비교한다.
            return false;
        }

        return true;
    }

    //장바구니에 담긴 상품 삭제
    public String deleteCartItem(Long cartItemId){
        CartItem cartItem = cartItemRepository
                .findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        String cartItemNm = cartItem.getProduct().getProductName();

        cartItemRepository.delete(cartItem);

        return cartItemNm;

    }

    //장바구니에 담긴 상품의 수량 변경
    public void updateCartItemCount(Long cartItemId, Long count){
        //넘겨받은 cartItemId를 통해서 cartItem을 찾아온다
        CartItem cartItem = cartItemRepository
                .findById(cartItemId)
                .orElseThrow(FindException::new);

        //찾아온 cartItem의 count를 변경하면 찾아올 때 manager가 관리하므로
        //더티체킹 수행

        cartItem.updateCount(count);
    }

    //장바구니에서 준 id를 통해서 주문을 하고, 카트를 비운다.
    public List<OrdersDTO> orderCartItem(List<CartOrderDTO> cartOrderDTOList, String userId){

        List<OrdersDTO> orderDTOList = new ArrayList<>();

        for (CartOrderDTO cartOrderDTO : cartOrderDTOList) {
            //CartItem entity
            CartItem cartItem =
                    cartItemRepository
                            .findById(cartOrderDTO.getCartItemId())
                            .orElseThrow(EntityNotFoundException::new);

            OrdersDTO ordersDTO = new OrdersDTO(); //뷰단에서 받은 것처럼 객체만들기
            //원래 주문은 dto로 컨트롤러에서부터 받는다.

            ordersDTO.setPno(cartItem.getProduct().getPno()); //장바구니 아이템id > 주문아이템id
            ordersDTO.setCount(cartItem.getOrderCount());

            orderDTOList.add(ordersDTO); //orderDTOList에 추가해준다.

        }

        // Long orderId = ordersService.order(orderDTOList, userId); //주문테이블에 저장

        //카트 삭제

        for (CartOrderDTO cartOrderDTO : cartOrderDTOList) {
            CartItem item = cartItemRepository
                    .findById(cartOrderDTO.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);

            cartItemRepository.delete(item);
        }

        return orderDTOList;


    }




}
