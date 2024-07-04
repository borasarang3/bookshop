package com.example.bookshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table
@Getter
@Setter
@ToString
public class OrdersItem {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ordersItemId; //주문아이템Id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id", nullable = false)
    private Orders orders; //주문ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pno", nullable = false)
    private Product product;   //상품ID

    @Column(nullable = false)
    private Long orderPrice;

    @Column(nullable = false)
    private Long orderCount; //수량 장바구니 제품당 담는 수량


    public static OrdersItem createOrdersItem(Product product, Long orderCount){
        //product는 검색한 아이템 //removeStock()를 통해서 수량 변경
        OrdersItem ordersItem = new OrdersItem();

        ordersItem.setProduct(product); //산 아이템
        ordersItem.setOrderCount(orderCount); //구매수량

        ordersItem.setOrderPrice(product.getProductPrice());
        product.removeStock(orderCount);

        return ordersItem;
    }

    public Long getTotalPrice(){ //주문한 금액
        return orderPrice * orderCount;
    }

    public void cancel(){
        this.getProduct().addStock(orderCount);
    }
}
