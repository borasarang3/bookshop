package com.example.bookshop.dto;

import com.example.bookshop.entity.OrdersItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OrdersItemDTO {
    //주문이력 / 구매이력 페이지에 필요한dto
    // 매출이력? 주문내역

    //생성자로 dto생성 기본생성자 X

    private String productName; //아이템명

    private Long count;  //수량

    private Long orderPrice; //주문금액

    private String imgUrl;  //상품이미지
    //상품 상세페이지(주문이 가능한 페이지) 비슷함

    //주문들의 정보를 담을 OrderHistDTO가 필요하다.
    // 한번에 여러가지의 아이템을 주문시 주문 아이템이 여러개라서
    // 묶어주는 주문 1번의 DTO가 필요

    public OrdersItemDTO(OrdersItem ordersItem, String imgUrl) {
        // 주문아이템을 받아서
        this.productName = ordersItem.getProduct().getProductName();
        this.count = ordersItem.getOrderCount();
        this.orderPrice = ordersItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }


}
