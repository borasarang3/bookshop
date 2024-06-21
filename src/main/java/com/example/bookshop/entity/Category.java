package com.example.bookshop.entity;

import com.example.bookshop.dto.CategoryDTO;
import com.example.bookshop.dto.UserDTO;
import com.example.bookshop.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

@Entity
@Getter
@Setter
@ToString
@Table(name = "category")
public class Category extends BaseEntity {

    @Id
    @Column(name = "cno")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno; //카테고리 넘버

    @Column(name = "cname", nullable = false, length = 10)
    private String cname; //카테고리 이름

    private static ModelMapper modelMapper = new ModelMapper();

    public static Category of(CategoryDTO categoryDTO){
        return modelMapper.map(categoryDTO, Category.class);
    }




}
