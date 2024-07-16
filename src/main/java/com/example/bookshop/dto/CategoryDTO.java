package com.example.bookshop.dto;

import com.example.bookshop.entity.Category;
import com.example.bookshop.entity.UserMember;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

import java.util.List;

@Getter
@Setter
@ToString
public class CategoryDTO {

    private Long cno; //카테고리 넘버

    @NotBlank(message = "카테고리 이름을 입력해주세요.")
    @Length(min=1, max=10, message = "카테고리 이름은 최대 10자까지입니다.")
    private String cname; //카테고리 이름

    private static ModelMapper modelMapper = new ModelMapper();

    public static CategoryDTO of(Category category){
        return modelMapper.map(category, CategoryDTO.class);
    }


}
