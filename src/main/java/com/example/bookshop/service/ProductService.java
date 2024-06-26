package com.example.bookshop.service;

import com.example.bookshop.dto.CategoryDTO;
import com.example.bookshop.dto.ImageDTO;
import com.example.bookshop.dto.ProductDTO;
import com.example.bookshop.entity.Category;
import com.example.bookshop.entity.Image;
import com.example.bookshop.entity.Product;
import com.example.bookshop.repository.CategoryRepository;
import com.example.bookshop.repository.ImageRepository;
import com.example.bookshop.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final ModelMapper modelMapper;

    public ProductDTO register(ProductDTO productDTO,
                               List<MultipartFile> multipartFiles) throws IOException {
        //상품 등록

        log.info("서비스에서 받은 값 : " + productDTO);

        Product product = Product.of(productDTO);

        Category category = categoryRepository
                .findById(productDTO.getCategoryid()).orElseThrow(EntityNotFoundException::new);
        product.setCategory(category);

        log.info("변환된 product : " + product);

        productRepository.save(product);

        //이미지 등록
        for (int i=0; i < multipartFiles.size(); i++){
            //입력받은 아이템 이미지 숫자만큼
            //하지만 우리가 만들어놓은건 5개라 5개 들어옴
            Image image = new Image();
            image.setProduct(product); // 이 아이템은 id를 가지고 있는가 저장되는가? 더티

            if (i == 0){
                image.setRepimgYn("Y"); // 대표 이미지
            } else {
                image.setRepimgYn("N");
            }

            imageService.saveImg(image, multipartFiles.get(i));

        }


        return productDTO;

    }

    public List<ProductDTO> list(){
        List<Product> productList = productRepository.findAll();

        productList.forEach(product -> log.info(product));

        List<ProductDTO> productDTOList = productList.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());

        for(Product product : productList){
            for(ProductDTO productDTO : productDTOList){
                if(product.getPno() == productDTO.getPno()){
                    productDTO.setCategoryid(product.getCategory().getCno());
                }
            }
        }

        //dto에 넣는 건 category가 아니라 categoryId(Long 타입)
        //엔티티의 pno와 dto의 pno가 같을 때,
        //엔티티의 category Cno(id)를 dto에 set

        productDTOList.forEach(productDTO -> log.info(productDTO));

        //////////////
        //이미지 찾기

        List<Image> imageList = imageRepository.findAll();

        imageList.forEach(image -> log.info(image));

        List<ImageDTO> imageDTOList = imageList.stream()
                .map(image -> modelMapper.map(image, ImageDTO.class))
                .collect(Collectors.toList());

        imageDTOList.forEach(image -> log.info(image));

        // pno가 같은 productdto에 imageDTOList 넣어주기
        // 제미니가 알려준 코드 (검증 필요)
        //List<Product> productList = productRepository.findAll();

        //productList.forEach(product -> log.info(product));

//        List<ProductDTO> productDTOList = productList.stream()
//                .map(product -> {
//                    ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
//                    productDTO.setImages(product.getImages().stream()
//                            .map(image -> modelMapper.map(image, ImageDTO.class))
//                            .collect(Collectors.toList()));
//                    return productDTO;
//                })
//                .collect(Collectors.toList());
//
//        productDTOList.forEach(productDTO -> log.info(productDTO));




       // productDTO.setImageDTOList(imageDTOList);

        return productDTOList;
    }

    //상품 상세보기
    public ProductDTO productRead(Long pno) {

        Product product = productRepository.findById(pno)
                .orElseThrow(EntityNotFoundException::new);

        ProductDTO productDTO = ProductDTO.of(product);
        productDTO.setCategoryid(product.getCategory().getCno());

        /////////
        //이미지 찾기

        List<Image> imageList = imageRepository.findByProduct(product);

        imageList.forEach(image -> log.info(image));

        List<ImageDTO> imageDTOList = imageList.stream()
                .map(image -> modelMapper.map(image, ImageDTO.class))
                .collect(Collectors.toList());

        imageDTOList.forEach(image -> log.info(image));

        productDTO.setImageDTOList(imageDTOList);

        return productDTO;

    }

    //판매자 이름으로 등록한 상품 목록 찾기
    public List<ProductDTO> userProduct(String UserName){

        List<Product> productList = productRepository.findBySeller(UserName);

        log.info(productList);

        productList.forEach(product -> log.info(product));

        List<ProductDTO> productDTOList = productList.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());

        for(Product product : productList){
            for(ProductDTO productDTO : productDTOList){
                if(product.getPno() == productDTO.getPno()){
                    productDTO.setCategoryid(product.getCategory().getCno());
                }
            }
        }

        productDTOList.forEach(productDTO -> log.info(productDTO));

        return productDTOList;

    }

    //상품 수정
    public void productModify(ProductDTO productDTO) {

        log.info("service에서 받은 DTO 확인 : " + productDTO);

        Product product = Product.of(productDTO);

        log.info("product 확인 : " + product);

        Category category = categoryRepository
                .findById(productDTO.getCategoryid()).orElseThrow(EntityNotFoundException::new);
        product.setCategory(category);

        log.info("변환된 product : " + product);

        productRepository.save(product);

    }

    //상품 삭제
    public void productRemove(ProductDTO productDTO) {

        productRepository.deleteById(productDTO.getPno());


    }


}
