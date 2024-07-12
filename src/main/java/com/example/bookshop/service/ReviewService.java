package com.example.bookshop.service;

import com.example.bookshop.dto.PageRequestDTO;
import com.example.bookshop.dto.PageResponseDTO;
import com.example.bookshop.dto.ReviewDTO;
import com.example.bookshop.dto.UserDTO;
import com.example.bookshop.entity.Orders;
import com.example.bookshop.entity.Product;
import com.example.bookshop.entity.Review;
import com.example.bookshop.entity.UserMember;
import com.example.bookshop.repository.OrdersItemRepository;
import com.example.bookshop.repository.OrdersRepository;
import com.example.bookshop.repository.ReviewRepository;
import com.example.bookshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final OrdersRepository ordersRepository;
    private final OrdersItemRepository ordersItemRepository;

    //리뷰 등록
    public Review registerReview(ReviewDTO reviewDTO){

        //리뷰를 작성할 수 있는 건
        // 리뷰를 작성하려는 상품을 구매한 사람만
        // 리뷰를 작성하기 전에 주문 내역을 확인
        // 주문 내역에 해당 상품이 있는지 확인하고
        // 해당 상품이 없다면 작성불가

//        UserMember userMember = userRepository.findByUserName(reviewDTO.getReviewer());
//        log.info("userMember : " + userMember );
//        List<Orders> ordersList = ordersRepository.findByUserMemberUserId(userMember.getUserId());
//        log.info(ordersList);
//        if (ordersList != null){
//            Long result = ordersItemRepository.findorderspno(reviewDTO.getPno());
//            log.info(result);
//            if (result == null){
//                throw new IllegalStateException("리뷰를 작성할 수 없는 회원입니다.");
//            }
//        }

        Product product = new Product();
        product.setPno(reviewDTO.getPno());

        Review review = modelMapper.map(reviewDTO, Review.class);

        review.setProduct(product);
        review.setReviewer(review.getReviewer());

        return reviewRepository.save(review);
    };

    //리뷰 리스트
    public PageResponseDTO<ReviewDTO> getListofProduct(Long pno, PageRequestDTO pageRequestDTO){

        Page<Review> reviews =  reviewRepository.listOfProduct(pno, pageRequestDTO.getPageable("rno"));

        List<ReviewDTO> reviewDTOS = reviews.getContent().stream().map(review -> modelMapper.map(review, ReviewDTO.class)).collect(Collectors.toList());

        PageResponseDTO<ReviewDTO> reviewDTOPageResponseDTO = new PageResponseDTO<>(pageRequestDTO, reviewDTOS, (int) reviews.getTotalElements());




        return  reviewDTOPageResponseDTO;

    };

    //리뷰를 눌렀을 때 모달창에 띄워주기
    public ReviewDTO readReview(Long rno) {
        return modelMapper.map(reviewRepository.findById(rno).get(), ReviewDTO.class);
    };

    //리뷰 수정
    public void modify(ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(reviewDTO.getRno()).get();
        log.info("수정하려는 리뷰 : " + review);
        review.changeReview(reviewDTO.getReviewName(), review.getReviewContent());

        reviewRepository.save(review);
    };

    //리뷰 삭제
    public void remove(Long rno) {
        reviewRepository.deleteById(rno);
    };

    //리뷰 수 세기
    public Long countReview(Long pno) {
        Long count = reviewRepository.countByProductPno(pno);
        return count;
    };

}
