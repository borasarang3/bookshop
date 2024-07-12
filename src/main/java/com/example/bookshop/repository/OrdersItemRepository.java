package com.example.bookshop.repository;

import com.example.bookshop.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrdersItemRepository extends JpaRepository<Orders, Long> {

    @Query("select oi from OrdersItem oi where oi.product.pno = :pno")
    Long findorderspno(Long pno);
    //상품이 있는지 확인

}
