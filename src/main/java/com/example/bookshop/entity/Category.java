package com.example.bookshop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "category")
public class Category {

    @Id
    @Column(name = "cname", nullable = false, length = 10)
    private String cname; //카테고리 이름



}
