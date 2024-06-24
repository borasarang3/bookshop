package com.example.bookshop.service;

import com.example.bookshop.dto.ProductDTO;
import com.example.bookshop.entity.Category;
import com.example.bookshop.entity.Product;
import com.example.bookshop.repository.CategoryRepository;
import com.example.bookshop.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private  final CategoryRepository categoryRepository;

    public ProductDTO register(ProductDTO productDTO){
        //상품 등록

        log.info("서비스에서 받은 값 : " + productDTO);

        Product product = Product.of(productDTO);

        Category  category =categoryRepository.findById(productDTO.getCategory()).orElseThrow(EntityNotFoundException::new);
        product.setCategory(category);
        log.info("변환된 product : " + product);

        productRepository.save(product);

        return productDTO;


    }


}
