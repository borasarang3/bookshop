package com.example.bookshop.service;

import com.example.bookshop.dto.CategoryDTO;
import com.example.bookshop.dto.UserDTO;
import com.example.bookshop.entity.Category;
import com.example.bookshop.entity.UserMember;
import com.example.bookshop.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    //전체 카테고리 갖고 오기
    public List<CategoryDTO> allCategoryList(){

        List<Category> categoryList = categoryRepository.findAll();

        List<CategoryDTO> categoryDTOList = categoryList.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());

        categoryDTOList.forEach(categoryDTO -> log.info(categoryDTO));

        return categoryDTOList;


    }

    //아이디로 카테고리 갖고 오기
    public CategoryDTO findCategoryId(Long cno){

        Category category = categoryRepository.findById(cno)
                .orElseThrow(EntityNotFoundException::new);

        CategoryDTO categoryDTO = CategoryDTO.of(category);

        return categoryDTO;

    }


    //카테고리 등록
    public CategoryDTO register (String cname){

        log.info("서비스에 온 카테고리 이름 : " + cname);

        CategoryDTO categoryDTO = new CategoryDTO();

        categoryDTO.setCname(cname);

        Category category = Category.of(categoryDTO);

        Category category1 = categoryRepository.findByCname(category.getCname());

        if (category1 == null){

            categoryRepository.save(category);

            return categoryDTO;
        }

        log.info("이미 등록된 카테고리 이름입니다.");

        return null;

    }

    public CategoryDTO modify(String cname, String cname1) {
        log.info("서비스 수정하려는 카테고리 이름 : " + cname1);
        log.info("서비스에 온 카테고리 이름 : " + cname);

        //데이터베이스에서 저장된 이름을 갖고 와서
        //수정 입력창에서 받은 이름을
        //데이터베이스에 저장

        Category category = categoryRepository.findByCname(cname1);

        log.info(category.getCname());

        if (category.getCname().equals(cname)){
            log.info("이미 등록된 카테고리 이름입니다.");

            return null;

        } else {
            category.setCname(cname);
            categoryRepository.save(category);

            CategoryDTO categoryDTO = CategoryDTO.of(category);

            return categoryDTO;
        }

    }

    public void remove(String cname) {

        log.info("서비스에 온 카테고리 이름 : " + cname);

        Category category = categoryRepository.findByCname(cname);

        if (category == null){
            log.info("null임");
            throw new IllegalStateException("카테고리를 찾을 수 없습니다.");
        }

        categoryRepository.deleteById(category.getCno());

    }




}
