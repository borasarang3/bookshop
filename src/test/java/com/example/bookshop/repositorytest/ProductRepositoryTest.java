package com.example.bookshop.repositorytest;

import com.example.bookshop.dto.ProductDTO;
import com.example.bookshop.entity.Product;
import com.example.bookshop.service.ProductService;
import lombok.extern.log4j.Log4j2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@Log4j2
class ProductRepositoryTest {

    @Autowired
    ProductService productService;

    @Test
    public void findterst(){
        productService.userProduct(null).forEach(a -> log.info("객체 : " + a));

        for( ProductDTO productDTO : productService.userProduct(null)){
                log.info(productDTO);
        }

    }

}