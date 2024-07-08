package com.example.bookshop.repository;

import com.example.bookshop.entity.Orders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    //주문한 목록 받아오기
    @Query("select o " +
            "from Orders o where o.userMember.userId = :userId " +
            "order by o.orderDate desc")
    List<Orders> findOrders(@Param("userId") String userId, Pageable pageable);

    //주문 수량
    //유저아이디를 기준으로
    @Query("select count(o) from Orders o " +
            "where o.userMember.userId = :userId")
    Long countOrder(@Param("userId") String userId);

}
