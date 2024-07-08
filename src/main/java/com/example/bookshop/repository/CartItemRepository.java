package com.example.bookshop.repository;

import com.example.bookshop.dto.CartDetailDTO;
import com.example.bookshop.entity.Cart;
import com.example.bookshop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCart_CartIdAndProductPno(Long cartId, Long pno);
    //이미 상품이 장바구니에 있다면 add를 해주기 위해서

    // 장바구니 페이지에 전달할 CartDetailDTO 리스트를 쿼리 하나로 조회하는 jpQL문
    // Long CartItemId, String itemNm, int price, int count, String imgUrl

    @Query("select new com.example.bookshop.dto.CartDetailDTO(ci.cartItemId, p.pno, p.productName, p.productPrice, ci.orderCount, im.imgUrl) " +
            "from CartItem ci , Image im join ci.product p " +
            "where ci.cart.cartId = :cartId and im.product.pno = ci.product.pno " +
            "and im.repimgYn = 'Y' order by ci.regidate desc")
    List<CartDetailDTO> findCartDetail(Long cartId);

    //CartItem findByCreateBy (String userId);

}

