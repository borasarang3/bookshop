package com.example.bookshop.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrdersDTO {

    //사용자는 제품을 클릭할뿐 아이디를 입력하지 않음
    @NotNull(message = "상품 아이디는 필수 입력값입니다.")
    private Long pno; //내가 주문하는 상품ID

    //이 값은 입력을 받음
    @Min(value = 1, message = "최소 주문 수량은 1개입니다.")
    @Max(value = 999, message = "최대 주문 수량은 999개입니다.")
    private Long count; //내가 주문한 상품 수량

}
