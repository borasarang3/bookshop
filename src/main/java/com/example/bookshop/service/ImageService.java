package com.example.bookshop.service;

import com.example.bookshop.dto.ImageDTO;
import com.example.bookshop.dto.ProductDTO;
import com.example.bookshop.entity.Image;
import com.example.bookshop.entity.Product;
import com.example.bookshop.repository.ImageRepository;
import com.example.bookshop.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class ImageService {

    //상품 이미지 저장 장소
    @Value("${productImgLocation}")
    private String productImgLocation;

    //리뷰 이미지 저장 장소
    @Value("${reviewImgLocation}")
    private String reviewImgLocation;

    //DB 저장용
    private final ImageRepository imageRepository;
    //물리적인 저장용
    private final FileService fileService;

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public void saveImg(Image image, MultipartFile multipartFile) throws IOException {
        // 화면에서 넘겨받은 이미지 파일 multipartFile에서 파일명을 반환하는 메소드
        String oriImgName = multipartFile.getOriginalFilename();
        // 선언만 아래에서 변환
        String imgName = "";
        String imgUrl = "";

        // 파일업로드
        // 입력받은 multipartFile 파일명이 없다면
        // 화면단에서 파일업로드 폼만 있고 이미지를 넣지 않았다면 name="itemImgFile" 의
        // getOriginalFilename() 실행시 결과값이 "" << null이 아니다.
//        if (oriImgName != null && oriImgName != "" && oriImgName.length() != 0){
//            //아래와 같은 조건 // oriImgName 이 비어있지 않을 때
//        }

        if (!StringUtils.isEmpty(oriImgName)){
            if (image.getProduct() != null){
                imgName = fileService.uploadFile(productImgLocation, oriImgName,
                        multipartFile.getBytes());
            }
//            if (리뷰id != null일 때도 만들기)


            //확장자를 제외하고 uuid를 붙인 경로
            if (image.getProduct() != null){
                imgUrl = "/images/product/" + imgName;
            }


            // 상품 이미지 정보 저장 DB
            // 파라미터로 입력받은 Image entity 에 가공된 데이터를 set!!!!!


        }

        image.updateImg(oriImgName, imgName, imgUrl);
        imageRepository.save(image);
    }

    // 업데이트

    public void updateImage (Long ino, MultipartFile multipartFile) throws Exception {
        //이미지 파일 비어있는지 확인 // 이유는 우리는 화면에서 input file을 다 열어놔서
        // 빈 값이 올 수 있음
        if (!multipartFile.isEmpty()) {
            //이미지 가져오기 아이디를 가지고
            Image savedItemImg = imageRepository
                    .findById(ino).orElseThrow(EntityNotFoundException::new);

            //기존 물리적 이미지파일을 삭제
            if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
                if (savedItemImg.getProduct() != null){
                    fileService.deleteFile(productImgLocation + "/" + savedItemImg.getImgName() );
                }
            }

            // 입력받은 이미지 파일의 이름을 가져와서 fileService에 있는 물리적인 파일 저장
            String orImgName = multipartFile.getOriginalFilename();
            if (savedItemImg.getProduct() != null){
                String imgName = fileService
                        .uploadFile(productImgLocation, orImgName, multipartFile.getBytes());
                // /images/item
                String imgUrl = "/images/product/" + imgName;
                // 엔티티는 현재 영속 상태이므로 데이터를 변경하는 것만으로도
                // 변경감지기능 동작하여 트랜잭션이 끝날때 update 쿼리 실행 더티체킹
                savedItemImg.updateImg(orImgName, imgName, imgUrl);
            }

        }
    }





}
