package com.example.bookshop.entity;

import com.example.bookshop.constant.ItemSellStatus;
import com.example.bookshop.dto.ProductDTO;
import com.example.bookshop.entity.base.BaseEntity;
import com.example.bookshop.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

@Entity
@Getter
@Setter
@ToString
@Table(name = "product")
public class Product extends BaseEntity {

    @Id
    @Column(name = "pno", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno; //상품 넘버

    @Column(nullable = false)
    private String seller; //판매자 이름

    @Column(nullable = false, length = 50)
    private String productName; //상품 이름

    @Column(nullable = false)
    private String writer; //글쓴이

    @Column(nullable = false)
    private String publish; //출판사

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String productContent; //상품 설명

    @Column(nullable = false)
    private Long productPrice; //상품 가격

    @Column(nullable = false)
    private Long productAmount; //상품 수량

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cno", nullable = false)
    private Category category; //카테고리

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //상품 판매 상태

    private static ModelMapper modelMapper = new ModelMapper();

    public static Product of(ProductDTO productDTO) {
        return modelMapper.map(productDTO, Product.class);
    }

    //수량을 입력받아서 db의 저장된 개수 확인과, 수량 변경
    public void removeStock(Long productAmount){ // 구매 수량
        //이미 이 entity는 select를 통해서 값을 가져와서
        //entitymanager가 데이터를 가지고 있다.
        //그래서 수정이 가능하다
        Long restStock = this.productAmount - productAmount;
        if (restStock < 0) {
            throw new OutOfStockException("상품의 재고가 부족합니다. " +
                    "(현재 재고수량 : " + this.productAmount + ")");
        }
        this.productAmount = restStock;

        // 추가사항 : 만약에 현재수량 - 구매수량이 딱 0이면 SOLD_OUT
        if(restStock == 0) {
            this.itemSellStatus = ItemSellStatus.SOLD_OUT;
        }

    }


    //취소를 눌렀을 때
    public void addStock(Long productAmount){ //구매한 수량을 취소할 때 수정

        this.productAmount += productAmount;
    }




}
