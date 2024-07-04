package com.example.bookshop.repository;

import com.example.bookshop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // usermemberId로 저장된 카트 객체 가져오기
    // 장바구니의 id를 cartItem이 참조
    // 사용자 아이디로 저장된 email를 찾으려면 CreateBy
    Cart findByUserMemberUserId(String userId);

//    Cart findByCreateBy(Long userId);




}
