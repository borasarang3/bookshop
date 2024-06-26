package com.example.bookshop.dto;

import com.example.bookshop.entity.Image;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class ImageDTO {

    private Long ino; //이미지 Id

    private String imgName; //이미지 이름

    private String oriImgName; //이미지 파일명

    private String imgUrl; //이미지 조회 경로

    private Long pno; //상품 번호



    private static ModelMapper modelMapper = new ModelMapper();

    public static ImageDTO of(Image image) {
        return modelMapper.map(image, ImageDTO.class);
    }

}
