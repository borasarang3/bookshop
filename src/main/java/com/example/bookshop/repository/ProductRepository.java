package com.example.bookshop.repository;

import com.example.bookshop.dto.ProductDTO;
import com.example.bookshop.entity.Product;
import com.example.bookshop.repository.search.ProductRepositoryCustom;
import com.example.bookshop.repository.search.ProductSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, QuerydslPredicateExecutor<Product>, ProductRepositoryCustom, ProductSearch {

    @Query("select seller, productName, writer, publish, productContent," +
            "productPrice, productAmount, category.cname, itemSellStatus " +
            "from Product ")
    List<Product> findAllProduct();

    List<Product> findBySeller(String seller);

    @Query("select p from  Product p where pno = :pno")
    Product ororor(@Param("pno")Long pno);


}
