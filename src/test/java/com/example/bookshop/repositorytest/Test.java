package com.example.bookshop.repositorytest;

import com.example.bookshop.service.ProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class Test {
    @Autowired
    ProductService productService;

    @org.junit.jupiter.api.Test
    public void testtt(){

    }
}
