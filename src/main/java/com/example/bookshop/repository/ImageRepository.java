package com.example.bookshop.repository;

import com.example.bookshop.entity.Image;
import com.example.bookshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByProduct (Product product);

    Image findByProductPnoAndRepimgYn(Long pno, String repimgYn);


}
