package com.example.bookshop.service;

import com.example.bookshop.dto.CategoryDTO;
import com.example.bookshop.dto.ProductDTO;
import com.example.bookshop.entity.Category;
import com.example.bookshop.entity.Product;
import com.example.bookshop.repository.CategoryRepository;
import com.example.bookshop.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class ProductService {

    private final ProductRepository productRepository;
    private  final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public ProductDTO register(ProductDTO productDTO){
        //상품 등록

        log.info("서비스에서 받은 값 : " + productDTO);

        Product product = Product.of(productDTO);

        Category category = categoryRepository
                .findById(productDTO.getCategoryid()).orElseThrow(EntityNotFoundException::new);
        product.setCategory(category);

        log.info("변환된 product : " + product);

        productRepository.save(product);

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

        return productDTOList;
    }

    //상품 상세보기
    public ProductDTO productRead(Long pno) {

        Product product = productRepository.findById(pno)
                .orElseThrow(EntityNotFoundException::new);

        ProductDTO productDTO = ProductDTO.of(product);

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





}
