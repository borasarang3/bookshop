package com.example.bookshop.entity;

import com.example.bookshop.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "review")
@Getter
@Setter
@ToString
public class Review extends BaseEntity {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno; //리뷰ID

    @Column(nullable = false, length = 50)
    private String reviewName;

    @Column(nullable = false)
    private String reviewContent;

    @Column(nullable = false)
    private String reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product; //참조대상

    public void changeReview(String reviewName, String reviewContent){
        this.reviewName = reviewName;
        this.reviewContent = reviewContent;

    }

}
