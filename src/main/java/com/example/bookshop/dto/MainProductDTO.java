package com.example.bookshop.dto;

import com.example.bookshop.constant.ItemSellStatus;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class MainProductDTO {

    private Long pno; //상품 넘버

    private String seller; //판매자 이름

    private String productName; //상품 이름

    private String writer; //글쓴이

    private String publish; //출판사

    private String productContent; //상품 설명

    private Long productPrice; //상품 가격

    private Long productAmount; //상품 수량

    private Long categoryid; //카테고리

    private ItemSellStatus itemSellStatus; //상품 판매 상태

    private LocalDateTime regidate; //상품 등록일

    private String imgUrl; //상품 이미지

    private Long reviewCount; //리뷰 수

    private List<ImageDTO> imageDTOList;

    public MainProductDTO setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public MainProductDTO setImageDTOList(List<ImageDTO> imageDTOList) {
        this.imageDTOList = imageDTOList;
        return this;
    }

    //생성자에 @QueryProjection 어노테이션을 선언하여 Querydsl로 결과 조회시
    // MainitemDTO 객체로 바로 받아오도록 활용
    // select * <<entity x >> dto로 바뀐다. from table

    public MainProductDTO(){}

    @QueryProjection
    public MainProductDTO(Long pno, String seller, String productName,
                          String writer, String publish, String productContent,
                          Long productPrice, Long productAmount, Long categoryid,
                          ItemSellStatus itemSellStatus, LocalDateTime regidate,
                          String imgUrl){
        this.pno = pno;
        this.seller = seller;
        this.productName = productName;
        this.writer = writer;
        this.publish = publish;
        this.productContent = productContent;
        this.productPrice = productPrice;
        this.productAmount = productAmount;
        this.categoryid = categoryid;
        this.itemSellStatus = itemSellStatus;
        this.regidate = regidate;
        this.imgUrl = imgUrl;

    }

    @QueryProjection
    public MainProductDTO(Long pno, String seller, String productName,
                          String writer, String publish, String productContent,
                          Long productPrice, Long productAmount, Long categoryid,
                          ItemSellStatus itemSellStatus, LocalDateTime regidate,
                          String imgUrl, Long reviewCount){
        this.pno = pno;
        this.seller = seller;
        this.productName = productName;
        this.writer = writer;
        this.publish = publish;
        this.productContent = productContent;
        this.productPrice = productPrice;
        this.productAmount = productAmount;
        this.categoryid = categoryid;
        this.itemSellStatus = itemSellStatus;
        this.regidate = regidate;
        this.imgUrl = imgUrl;
        this.reviewCount = reviewCount;

    }


}
