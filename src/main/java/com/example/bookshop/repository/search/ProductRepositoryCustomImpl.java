package com.example.bookshop.repository.search;

import com.example.bookshop.constant.ItemSellStatus;
import com.example.bookshop.dto.MainProductDTO;
import com.example.bookshop.dto.ProductSearchDTO;
import com.example.bookshop.dto.QMainProductDTO;
import com.example.bookshop.entity.Product;
import com.example.bookshop.entity.QImage;
import com.example.bookshop.entity.QProduct;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private JPAQueryFactory jpaQueryFactory;

    public ProductRepositoryCustomImpl(EntityManager em){
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    //상품명 검색 like
    private BooleanExpression productNameLike(String searchQuery){
        return StringUtils
                .isEmpty(searchQuery) ? null : QProduct.product.productName.like("%" + searchQuery + "%");
    }

    //판매상태 검색
    private BooleanExpression searchSellStatusEq(ItemSellStatus itemSellStatus) {
        // 입력값이 없으면 null 있으면 select * from item where itemSellStaus = :itemSellStatus
        return itemSellStatus == null ? null : QProduct.product.itemSellStatus.eq(itemSellStatus);
    }

    //등록일 기준 검색
    private BooleanExpression regDtsAfter(String searchDateType){
        // 6개월 전, 1년 전 1달 전 1주일 전

        LocalDateTime localDateTime = LocalDateTime.now(); //현재

        if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if (StringUtils.equals("1d", searchDateType)) {
            localDateTime = localDateTime.minusDays(1);
        } else if (StringUtils.equals("1w", searchDateType)) {
            localDateTime = localDateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)) {
            localDateTime = localDateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) {
            localDateTime = localDateTime.minusMonths(6);
        }

        return QProduct.product.regidate.after(localDateTime);

    }

    //제목, 내용, 작성자 검색
    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        // 제목, 제목+내용, 제목 + 내용 + 작성자 랑 검색어

        if (StringUtils.equals("productName", searchBy)) {
            //상품명
            return QProduct.product.productName.like("%" + searchQuery + "%");

        } else if (StringUtils.equals("writer", searchBy)){
            //글쓴이
            return QProduct.product.writer.like("%" + searchQuery + "%");

        } else if (StringUtils.equals("publish", searchBy)){
            //출판사
            return QProduct.product.publish.like("%" + searchQuery + "%");

        } else if (StringUtils.equals("seller", searchBy)) {
            //판매자
            return QProduct.product.seller.like("%" + searchQuery + "%");

        }

        return null;

    }




    @Override
    public Page<Product> getProductPage(ProductSearchDTO productSearchDTO, Pageable pageable) {
        //판매 상태, 등록일, 검색어로 검색
        //등록일 기준으로 내림차순

        QueryResults<Product> result = jpaQueryFactory.selectFrom(QProduct.product)
                .where( regDtsAfter(productSearchDTO.getSearchDateType()),
                        searchSellStatusEq(productSearchDTO.getItemSellStatus()),
                        searchByLike(productSearchDTO.getSearchBy(), productSearchDTO.getSearchQuery())
                )
                .orderBy(QProduct.product.pno.asc())
                .offset(pageable.getOffset()) // 몇 번부터 1번 글부터 //11번 글부터
                .limit(pageable.getPageSize()) //size = 10 //10
                .fetchResults();

        List<Product> content = result.getResults();

        long total = result.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainProductDTO> getMainProductPage(ProductSearchDTO productSearchDTO, Pageable pageable) {
        //searchQuery를 입력받아서 상품명과 비슷한 걸 찾는다.

        QProduct product = QProduct.product;
        QImage image = QImage.image;

        QueryResults<MainProductDTO> result = jpaQueryFactory.select(
                        new QMainProductDTO(
                                product.pno,
                                product.seller,
                                product.productName,
                                product.writer,
                                product.publish,
                                product.productContent,
                                product.productPrice,
                                product.productAmount,
                                product.category.cno,
                                product.itemSellStatus,
                                product.regidate,
                                image.imgUrl
                        )
                )
                .from(image)
                .join(image.product, product)
                .where(image.repimgYn.eq("Y")) //대표이미지
                .where(productNameLike(productSearchDTO.getSearchQuery())) //상품명 검색 입력받은 것과 같은 거
                .where(regDtsAfter(productSearchDTO.getSearchDateType()),
                       searchSellStatusEq(productSearchDTO.getItemSellStatus()),
                        searchByLike(productSearchDTO.getSearchBy(), productSearchDTO.getSearchQuery()))
                .orderBy(product.pno.asc())
                .offset(pageable.getOffset()) // 몇 번부터 1번 글부터 //11번 글부터
                .limit(pageable.getPageSize()) //size = 10 //10
                .fetchResults();

        List<MainProductDTO> content = result.getResults();
        long total = result.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainProductDTO> getMainProductCategoryPage(Long cno, ProductSearchDTO productSearchDTO, Pageable pageable) {
        //searchQuery를 입력받아서 상품명과 비슷한 걸 찾는다.

        QProduct product = QProduct.product;
        QImage image = QImage.image;

        QueryResults<MainProductDTO> result = jpaQueryFactory.select(
                        new QMainProductDTO(
                                product.pno,
                                product.seller,
                                product.productName,
                                product.writer,
                                product.publish,
                                product.productContent,
                                product.productPrice,
                                product.productAmount,
                                product.category.cno,
                                product.itemSellStatus,
                                product.regidate,
                                image.imgUrl
                        )
                )
                .from(image)
                .join(image.product, product)
                .where(image.repimgYn.eq("Y")) //대표이미지
                .where(product.category.cno.eq(cno)) //카테고리 번호와 같은 것
                .where(productNameLike(productSearchDTO.getSearchQuery())) //상품명 검색 입력받은 것과 같은 거
                .where(regDtsAfter(productSearchDTO.getSearchDateType()),
                        searchSellStatusEq(productSearchDTO.getItemSellStatus()),
                        searchByLike(productSearchDTO.getSearchBy(), productSearchDTO.getSearchQuery()))
                .orderBy(product.pno.asc())
                .offset(pageable.getOffset()) // 몇 번부터 1번 글부터 //11번 글부터
                .limit(pageable.getPageSize()) //size = 10 //10
                .fetchResults();

        List<MainProductDTO> content = result.getResults();
        long total = result.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainProductDTO> getMainProductPageDesc(ProductSearchDTO productSearchDTO, Pageable pageable) {
        //searchQuery를 입력받아서 상품명과 비슷한 걸 찾는다.

        QProduct product = QProduct.product;
        QImage image = QImage.image;

        QueryResults<MainProductDTO> result = jpaQueryFactory.select(
                        new QMainProductDTO(
                                product.pno,
                                product.seller,
                                product.productName,
                                product.writer,
                                product.publish,
                                product.productContent,
                                product.productPrice,
                                product.productAmount,
                                product.category.cno,
                                product.itemSellStatus,
                                product.regidate,
                                image.imgUrl
                        )
                )
                .from(image)
                .join(image.product, product)
                .where(image.repimgYn.eq("Y")) //대표이미지
                .where(productNameLike(productSearchDTO.getSearchQuery())) //상품명 검색 입력받은 것과 같은 거
                .where(regDtsAfter(productSearchDTO.getSearchDateType()),
                        searchSellStatusEq(productSearchDTO.getItemSellStatus()),
                        searchByLike(productSearchDTO.getSearchBy(), productSearchDTO.getSearchQuery()))
                .orderBy(product.regidate.desc())
                .offset(pageable.getOffset()) // 몇 번부터 1번 글부터 //11번 글부터
                .limit(pageable.getPageSize()) //size = 10 //10
                .fetchResults();

        List<MainProductDTO> content = result.getResults();
        long total = result.getTotal();

        return new PageImpl<>(content, pageable, total);
    }


}
