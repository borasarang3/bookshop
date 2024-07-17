package com.example.bookshop.service;

import com.example.bookshop.dto.*;
import com.example.bookshop.entity.*;
import com.example.bookshop.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
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
    private final CartItemRepository cartItemRepository;

    //나중에 지우기
    public List<ProductDTO> findOrderProduct(Long[] itemid){
        List<Product> productList = new ArrayList<>();
        List<ProductDTO> productDTOList = new ArrayList<>();
        for(Long id : itemid){
            CartItem cartItem = cartItemRepository.findById(id).get();
            Product product =  productRepository.ororor(cartItem.getProduct().getPno());
            if(product != null){
                productList.add(product);
                productDTOList.add(modelMapper.map(product, ProductDTO.class));
            }
        }

        for(Product product : productList) {
            for (ProductDTO productDTO : productDTOList) {

                    //product와 productDTO의 pno가 같을 때 이미지를 찾아서
                    //DTO에 set
                    List<Image> imageList = imageRepository.findByProduct(product);

                    imageList.forEach(image -> log.info(image));

                    List<ImageDTO> imageDTOList = imageList.stream()
                            .map(image -> modelMapper.map(image, ImageDTO.class))
                            .collect(Collectors.toList());

                    imageDTOList.forEach(image -> log.info(image));

                    List<Long> inos = new ArrayList<>();

                    for (Image image : imageList) {
                        for (ImageDTO imageDTO : imageDTOList) {
                            if (image.getProduct().getPno() == product.getPno()) {
                                imageDTO.setPno(image.getProduct().getPno());

                                inos.add(imageDTO.getIno());

                            }
                        }
                    }

                    imageDTOList.forEach(image -> log.info("최종 imageDTOList : " + image));

                    productDTO.setImageDTOList(imageDTOList);
                    productDTO.setInos(inos);


            }
        }

        productDTOList.forEach(productDTO -> log.info(productDTO));

        return productDTOList;


    }

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

    //Rest형식 페이징
    public PageResponseDTO<MainProductDTO> getMainofProduct(PageRequestDTO pageRequestDTO){

        Page<Product> mainProducts = productRepository.listofMain( pageRequestDTO.getPageable());

        List<MainProductDTO> mainProductDTOS
                = mainProducts.getContent().stream()
                .map(product -> modelMapper.map(product, MainProductDTO.class).setImageDTOList(imageRepository.findByProduct(product).stream().map(image -> modelMapper.map(image,ImageDTO.class)).collect(Collectors.toList())) ).collect(Collectors.toList());

        PageResponseDTO<MainProductDTO> mainProductDTOPageResponseDTO  = new PageResponseDTO<>(pageRequestDTO, mainProductDTOS, (int) mainProducts.getTotalElements());

        return mainProductDTOPageResponseDTO;

    }

    //검색+페이징+이미지 (내림차순 버전)
    @Transactional(readOnly = true)
    public Page<MainProductDTO> getProductImagPageDesc (ProductSearchDTO productSearchDTO,
                                                        Pageable pageable) {
        return productRepository.getMainProductPageDesc(productSearchDTO, pageable );
    }

    @Transactional(readOnly = true)
    public Page<MainProductDTO> getProductImagPageDescuser (ProductSearchDTO productSearchDTO,
                                                        Pageable pageable, String userId) {
        return productRepository.getMainProductPageDescuser(productSearchDTO, pageable, userId );
    }

    //검색+페이징+이미지+카테고리
    @Transactional(readOnly = true)
    public Page<MainProductDTO> getMainProductCategoryPage (Long cno, ProductSearchDTO productSearchDTO,
                                                    Pageable pageable) {
        return productRepository.getMainProductCategoryPage(cno, productSearchDTO, pageable);
    }


    //검색+페이징+이미지
    @Transactional(readOnly = true)
    public Page<MainProductDTO> getProductImagPage (ProductSearchDTO productSearchDTO,
                                                            Pageable pageable) {
        return productRepository.getMainProductPage(productSearchDTO, pageable);
    }

    //검색+페이징처리
    @Transactional(readOnly = true)
    public Page<Product> getProductPage (ProductSearchDTO productSearchDTO,
                                         Pageable pageable) {
        return productRepository.getProductPage(productSearchDTO, pageable);
    }

    //전체 상품 목록
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

                    //product와 productDTO의 pno가 같을 때 이미지를 찾아서
                    //DTO에 set
                    List<Image> imageList = imageRepository.findByProduct(product);

                    imageList.forEach(image -> log.info(image));

                    List<ImageDTO> imageDTOList = imageList.stream()
                            .map(image -> modelMapper.map(image, ImageDTO.class))
                            .collect(Collectors.toList());

                    imageDTOList.forEach(image -> log.info(image));

                    List<Long> inos = new ArrayList<>();

                    for(Image image : imageList) {
                        for (ImageDTO imageDTO : imageDTOList) {
                            if (image.getProduct().getPno() == productDTO.getPno()) {
                                imageDTO.setPno(image.getProduct().getPno());

                                inos.add(imageDTO.getIno());

                            }
                        }
                    }

                    imageDTOList.forEach(image -> log.info("최종 imageDTOList : " + image));

                    productDTO.setImageDTOList(imageDTOList);
                    productDTO.setInos(inos);

                }
            }
        }

        //dto에 넣는 건 category가 아니라 categoryId(Long 타입)
        //엔티티의 pno와 dto의 pno가 같을 때,
        //엔티티의 category Cno(id)를 dto에 set

        productDTOList.forEach(productDTO -> log.info(productDTO));

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
                .map(image ->modelMapper.map(image, ImageDTO.class))
                .collect(Collectors.toList());

        imageDTOList.forEach(image -> log.info(image));

        List<Long> inos = new ArrayList<>();

        //image를 product 객체로 받기 때문에 dto 변환시 pno가 null로 들어감
        //image와 product의 pno가 같을 때,
        //imageDTO에 pno를 set 해주는 동시에 inos 배열을 만들어
        //거기에 DTO의 ino를 set 해서 productDTO에 imageDTOList와 inos를 set

        for(Image image : imageList) {
            for (ImageDTO imageDTO : imageDTOList) {
                if (image.getProduct().getPno() == productDTO.getPno()) {
                    imageDTO.setPno(image.getProduct().getPno());

                    inos.add(imageDTO.getIno());

                }
            }
        }

        imageDTOList.forEach(image -> log.info("최종 imageDTOList : " + image));

        productDTO.setImageDTOList(imageDTOList);
        productDTO.setInos(inos);

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

                    List<Image> imageList = imageRepository.findByProduct(product);

                    imageList.forEach(image -> log.info(image));

                    List<ImageDTO> imageDTOList = imageList.stream()
                            .map(image -> modelMapper.map(image, ImageDTO.class))
                            .collect(Collectors.toList());

                    imageDTOList.forEach(image -> log.info(image));

                    List<Long> inos = new ArrayList<>();

                    for(Image image : imageList) {
                        for (ImageDTO imageDTO : imageDTOList) {
                            if (image.getProduct().getPno() == productDTO.getPno()) {
                                imageDTO.setPno(image.getProduct().getPno());

                                inos.add(imageDTO.getIno());

                            }
                        }
                    }

                    imageDTOList.forEach(image -> log.info("최종 imageDTOList : " + image));

                    productDTO.setImageDTOList(imageDTOList);
                    productDTO.setInos(inos);


                }
            }
        }

        productDTOList.forEach(productDTO -> log.info(productDTO));

        return productDTOList;

    }

    //상품 수정
    public void productModify(ProductDTO productDTO,
                              List<MultipartFile> multipartFiles) throws Exception {

        log.info("service에서 받은 DTO 확인 : " + productDTO);

        Product product = Product.of(productDTO);

        log.info("product 확인 : " + product);

        Category category = categoryRepository
                .findById(productDTO.getCategoryid()).orElseThrow(EntityNotFoundException::new);
        product.setCategory(category);

        log.info("변환된 product : " + product);

        productRepository.save(product);

        // 이미지
        List<Image> imageList = imageRepository.findByProduct(product);

        imageList.forEach(image -> log.info(image));

        List<ImageDTO> imageDTOList = imageList.stream()
                .map(image ->modelMapper.map(image, ImageDTO.class))
                .collect(Collectors.toList());

        imageDTOList.forEach(image -> log.info(image));

        List<Long> inos = new ArrayList<>();

        for(Image image : imageList) {
            for (ImageDTO imageDTO : imageDTOList) {
                if (image.getProduct().getPno() == productDTO.getPno()) {
                    imageDTO.setPno(image.getProduct().getPno());

                    inos.add(imageDTO.getIno());

                }
            }
        }

        imageDTOList.forEach(image -> log.info("최종 imageDTOList : " + image));

        productDTO.setImageDTOList(imageDTOList);
        productDTO.setInos(inos);

        //이미지 등록
        for (int i=0; i < multipartFiles.size(); i++){

            imageService
                    .updateImage(inos.get(i), multipartFiles.get(i));


        }


    }

    //상품 삭제
    public void productRemove(ProductDTO productDTO) throws Exception {

        Product product = productRepository.findById(productDTO.getPno())
                .orElseThrow(EntityNotFoundException::new);

        List<Image> imageList = imageRepository.findByProduct(product);
        for (Image image : imageList){
            imageService.deleteImage(image.getIno());
            imageRepository.deleteById(image.getIno());
        }

        productRepository.deleteById(productDTO.getPno());


    }


}
