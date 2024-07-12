package com.example.bookshop.dto;

import com.example.bookshop.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private Long rno; //리뷰ID

    private Long pno; //상품ID

    @NotBlank(message = "리뷰 이름을 적어주세요.")
    @Length(max = 50, message = "리뷰 이름은 최대 50자까지입니다.")
    private String reviewName;

    @NotBlank(message = "리뷰 내용을 적어주세요.")
    private String reviewContent;

    @NotBlank(message = "리뷰 작성자는 빈칸일 수 없습니다." )
    private String reviewer;

    private LocalDateTime regidate; //리뷰 등록일

    private LocalDateTime modidate; //리뷰 수정일

    private String createBy; //리뷰 생성자

    //이미지 파일
    public List<ImageDTO> imageDTOList = new ArrayList<>();
    //이미 저장되어서 수정할 때 불러온 사진들의 아이디 삭제할 이미지들
    private List<Long> inos = new ArrayList<>();


}
