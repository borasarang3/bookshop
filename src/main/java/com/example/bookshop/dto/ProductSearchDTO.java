package com.example.bookshop.dto;

import com.example.bookshop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductSearchDTO {

    private String searchDateType; //등록 일시
    //상품의 등록일 기준으로

    //판매중 //판매중지
    private ItemSellStatus itemSellStatus;

    //판매자로
    private String searchBy;

    //keyword
    private String searchQuery = "";

}
