package com.example.bookshop.service;

import com.example.bookshop.dto.ProductDTO;
import com.example.bookshop.entity.Product;
import com.example.bookshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class ProductService {

    private final ProductRepository productRepository;

    public ProductDTO register(ProductDTO productDTO){
        //상품 등록

        log.info("서비스에서 받은 값 : " + productDTO);

        Product product = Product.of(productDTO);

        productRepository.save(product);

        return productDTO;


    }


}
