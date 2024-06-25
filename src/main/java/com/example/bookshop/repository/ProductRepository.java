package com.example.bookshop.repository;

import com.example.bookshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select seller, productName, writer, publish, productContent," +
            "productPrice, productAmount, category.cname, itemSellStatus " +
            "from Product ")
    List<Product> findAllProduct();

    List<Product> findBySeller(String seller);


}
