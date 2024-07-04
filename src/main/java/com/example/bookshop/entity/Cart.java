package com.example.bookshop.entity;

import com.example.bookshop.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString
public class Cart extends BaseEntity {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId; //카트ID

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserMember userMember; //회원

    //장바구니 만들기
    public static Cart createCart(UserMember userMember) {
        Cart cart = new Cart();
        cart.setUserMember(userMember);
        return cart;
    }


}
