package com.example.bookshop.repository.search;

import com.example.bookshop.dto.MainProductDTO;
import com.example.bookshop.dto.ProductSearchDTO;
import com.example.bookshop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    //이미지 없이 product 내용만
    Page<Product> getProductPage(ProductSearchDTO productSearchDTO, Pageable pageable);

    //product + 이미지
    Page<MainProductDTO> getMainProductPage(ProductSearchDTO productSearchDTO, Pageable pageable);

    //내림차순
    Page<MainProductDTO> getMainProductPageDesc(ProductSearchDTO productSearchDTO, Pageable pageable);

}
