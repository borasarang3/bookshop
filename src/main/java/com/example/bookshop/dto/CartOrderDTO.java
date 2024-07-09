package com.example.bookshop.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CartOrderDTO {

    private Long  cartItemId;

    // 장바구니에서 여러개의 상품을 주문하므로 cartorderDTO 클래스가 자기
    // 자신을 list로 가지고 있도록

    private List<CartOrderDTO> cartOrderDTOList;



}
