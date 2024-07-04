package com.example.bookshop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CartItemDTO {

    @NotNull(message = "상품 아이디는 필수 입력 값입니다.")
    private Long pno;

    @Min(value = 1, message = "최소 1개 이상 담아주세요")
    private Long count;


}
