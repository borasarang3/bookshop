package com.example.bookshop.controller;

import com.example.bookshop.constant.Role;
import com.example.bookshop.dto.UserDTO;
import com.example.bookshop.entity.User;
import com.example.bookshop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    //회원가입 페이지
    @GetMapping("/register")
    public String userRegister(Model model){

        UserDTO userDTO = new UserDTO();
        userDTO.setRole(Role.USER);

        model.addAttribute("userDTO", userDTO);

        return "/user/register";
    }

    //어드민용 회원가입 페이지
    @GetMapping("/adminregister")
    public String adminRegister(Model model){

        UserDTO userDTO = new UserDTO();
        userDTO.setRole(Role.ADMIN);

        model.addAttribute("userDTO", userDTO);

        return "/user/register";
    }

    //회원가입
    @PostMapping("/register")
    public String RegisterPost(@Valid UserDTO userDTO,
                               BindingResult bindingResult,
                               Model model){

       if (bindingResult.hasErrors()){
           log.info("회원가입 오류 : " + bindingResult.hasErrors());
           return "/user/register";
       }

       try {
           User user = User
                   .createUser(userDTO, passwordEncoder);
           userService.saveUser(user);
       } catch (IllegalStateException e) {

           model.addAttribute("error", e.getMessage());
           return "/user/register";

       }

        return "/";
    }

    //회원정보 상세 확인
    @GetMapping ("/read")
    public void userRead() {
        //post 접근 불가로 get 변경해둠

    }

    //회원정보 수정 페이지 이동
    @PostMapping("/modify")
    public void userModify() {

    }

    //회원정보 수정
    @PostMapping("/modifypost")
    public void userModifyPost() {

    }

    //회원탈퇴
    @PostMapping("/remove")
    public void userRemove() {

    }

    //회원목록
    @GetMapping("/list")
    public void userList() {
        //post 접근 불가로 get 변경해둠
    }

    //구매 이력 확인
    @GetMapping("/buy")
    public void userBuy() {
        //post 접근 불가로 get 변경해둠
    }

    //등록한 상품 확인
    @GetMapping("/product")
    public void userProduct() {
        //post 접근 불가로 get 변경해둠
    }

    //작성한 리뷰 확인
    @GetMapping("/review")
    public void userReview() {
        //post 접근 불가로 get 변경해둠
    }








}
