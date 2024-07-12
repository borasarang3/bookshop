package com.example.bookshop.repository;

import com.example.bookshop.entity.Product;
import com.example.bookshop.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r where r.product.pno = :pno")
    List<Review> listReviewFromPno(Long pno);
    //상품 아이디로 상품에 달린 리뷰 찾아오기

    List<Review> findByProduct(Product product);
    //상품 entity를 받아서 받아온 상품을 찾는 쿼리 메소드

    @Query("select r from Review r where r.product.pno = :pno")
    Page<Review> listOfProduct(Long pno, Pageable pageable);
    //페이징처리된 리스트 불러오기

    //리뷰수
    Long countByProductPno(Long pno);

}
