package com.example.bookshop.entity;

import com.example.bookshop.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table
@Getter
@Setter
@ToString
public class CartItem extends BaseEntity {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId; //장바구니ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;      // 장바구니

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pno", nullable = false)
    private Product product;      // 아이템

    @Column(nullable = false)
    private Long orderCount; //수량 장바구니 제품당 담는 수량

    // 카트에 담길 아이템을 참조하는 cartItem
    public static CartItem CreateCartItem (Cart cart, Product product, Long orderCount) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setOrderCount(orderCount);
        return cartItem;

        // CartItem cartItem = CartItem.createCartItem(cart, item, count);
        // 만들어진 객체로 저장을 할걸?
    }

    public void addCount (Long orderCount) {
        this.orderCount += orderCount;
    }

    public void updateCount (Long orderCount) { this.orderCount = orderCount;}



}
