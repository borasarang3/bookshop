package com.example.bookshop.controller;

import com.example.bookshop.dto.PageRequestDTO;
import com.example.bookshop.dto.PageResponseDTO;
import com.example.bookshop.dto.ReviewDTO;
import com.example.bookshop.dto.UserDTO;
import com.example.bookshop.entity.Review;
import com.example.bookshop.service.ReviewService;
import com.example.bookshop.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/review")
public class ReviewRestController {

    private final ReviewService reviewService;
    private final UserService userService;

    //리뷰 리스트
    @GetMapping( "/list/{pno}")
    public @ResponseBody ResponseEntity ReviewRead(@PathVariable("pno") Long pno,
                                                   PageRequestDTO pageRequestDTO){

        //rest에서는 model 소용없음! model은 페이지를 새로 띄우겠다는 표시

        log.info("현재 본문 : " + pno);

        log.info(pageRequestDTO);

        PageResponseDTO<ReviewDTO> pageResponseDTO = reviewService.getListofProduct(pno, pageRequestDTO);

        return new ResponseEntity(pageResponseDTO, HttpStatus.OK);

    }

    //리뷰 등록
    @PostMapping("/register")
    public void ReviewRegister(@RequestBody ReviewDTO reviewDTO,
                               RedirectAttributes redirectAttributes,
                               Principal principal){
        //로그인 안 한 건 ajax에서 처리
        log.info("작성하는 리뷰 : " + reviewDTO);

       // UserDTO userDTO = userService.read(principal.getName());

       // reviewDTO.setReviewer(userDTO.getUserName());

        Review review = reviewService.registerReview(reviewDTO);
        log.info(review);

    }
    @PutMapping("/modify/{rno}")
    public void ReviewModify(@PathVariable("rno") Long rno,
                             @RequestBody ReviewDTO reviewDTO){

        reviewDTO.setRno(rno);

        reviewService.modify(reviewDTO);

    }

    @DeleteMapping("/remove/{rno}")
    public void ReviewRemove(@PathVariable("rno") Long rno){

        reviewService.remove(rno);

    }


}
