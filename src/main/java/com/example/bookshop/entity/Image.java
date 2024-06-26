package com.example.bookshop.entity;

import com.example.bookshop.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "image")
public class Image extends BaseEntity {

    @Id
    @Column(name = "ino", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ino; //이미지 Id

    private String imgName; //이미지 이름

    private String oriImgName; //이미지 파일명

    private String imgUrl; //이미지 조회 경로

    private String repimgYn; // 대표이미지 여부

    //상품 만들고 상품의 product_id 와 FK 맺기 이름 pno
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pno")
    private Product product;

    //리뷰 만들고 리뷰의 rno 와 FK 맺기 이름 rno


    public void updateImg(String imgName, String oriImgName, String imgUrl){

        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;

    }


}
