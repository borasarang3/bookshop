package com.example.bookshop.dto;

import com.example.bookshop.constant.ItemSellStatus;
import com.example.bookshop.entity.Category;
import com.example.bookshop.entity.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@ToString
public class ProductDTO {

    private Long pno; //상품 넘버

    @NotBlank(message = "판매자 이름을 적어주세요." )
    private String seller; //판매자 이름

    @NotBlank(message = "상품명을 적어주세요.")
    @Length(max = 50, message = "상품명은 최대 50자까지입니다.")
    private String productName; //상품 이름

    @Column(nullable = false)
    @NotBlank(message = "글쓴이를 적어주세요.")
    private String writer; //글쓴이

    @NotBlank(message = "출판사를 적어주세요.")
    private String publish; //출판사

    @NotBlank(message = "상품 설명을 적어주세요.")
    private String productContent; //상품 설명

    @NotNull(message = "상품 가격을 적어주세요.")
    private Long productPrice; //상품 가격

    @NotNull(message = "상품 재고를 적어주세요.")
    private Long productAmount; //상품 수량

    @NotNull(message = "카테고리를 설정해주세요.")
    private Long categoryid; //카테고리

    @NotNull
    private ItemSellStatus itemSellStatus; //상품 판매 상태

    private static ModelMapper modelMapper = new ModelMapper();

    public static ProductDTO of(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }


}
