package com.example.bookshop.entity;

import com.example.bookshop.constant.ItemSellStatus;
import com.example.bookshop.dto.ProductDTO;
import com.example.bookshop.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

@Entity
@Getter
@Setter
@ToString
@Table(name = "product")
public class Product extends BaseEntity {

    @Id
    @Column(name = "pno", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno; //상품 넘버

    @Column(nullable = false)
    private String seller; //판매자 이름

    @Column(nullable = false, length = 50)
    private String productName; //상품 이름

    @Column(nullable = false)
    private String writer; //글쓴이

    @Column(nullable = false)
    private String publish; //출판사

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String productContent; //상품 설명

    @Column(nullable = false)
    private Long productPrice; //상품 가격

    @Column(nullable = false)
    private Long productAmount; //상품 수량

    @OneToOne
    @JoinColumn(name = "cno", nullable = false)
    private Category category; //카테고리

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //상품 판매 상태

    private static ModelMapper modelMapper;

    public static Product of(ProductDTO productDTO) {
        return modelMapper.map(productDTO, Product.class);
    }



}
