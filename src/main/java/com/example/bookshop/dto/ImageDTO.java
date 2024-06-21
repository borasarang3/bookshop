package com.example.bookshop.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ImageDTO {

    private Long ino; //이미지 Id

    private String imgname; //이미지 이름

    private String imgorigname; //이미지 파일명

    private String imgUrl; //이미지 조회 경로

    private Long pno; //상품 번호

}
