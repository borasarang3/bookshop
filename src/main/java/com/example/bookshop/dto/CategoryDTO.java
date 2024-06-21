package com.example.bookshop.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
public class CategoryDTO {

    @NotBlank(message = "카테고리 이름을 입력해주세요.")
    @Length(min=1, max=10, message = "카테고리 이름은 최대 10자까지입니다.")
    private String cname; //카테고리 이름

}
