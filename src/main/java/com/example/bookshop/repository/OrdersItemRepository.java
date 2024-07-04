package com.example.bookshop.repository;

import com.example.bookshop.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersItemRepository extends JpaRepository<Orders, Long> {
}
