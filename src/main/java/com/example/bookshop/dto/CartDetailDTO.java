package com.example.bookshop.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CartDetailDTO {

    private Long cartItemId;    //장바구니 상품 아이디
    //게시판에서 bno처럼 아이템의 아이디를 가지고
    //차후에 주문을 하던가 삭제를 하는데 사용

    private Long pno; //상품 번호

    private String productName;  //상품명

    private Long productPrice;  //상품 금액

    private Long count;   //수량

    private String imgUrl;  //상품 이미지 경로

    //생성자
    public CartDetailDTO(Long cartItemId, Long pno, String productName, Long productPrice, Long count, String imgUrl) {
        this.cartItemId = cartItemId;
        this.pno = pno;
        this.productName = productName;
        this.productPrice = productPrice;
        this.count = count;
        this.imgUrl = imgUrl;
    }



}
