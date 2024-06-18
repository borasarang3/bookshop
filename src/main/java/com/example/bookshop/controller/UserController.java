package com.example.bookshop.controller;

import com.example.bookshop.constant.Role;
import com.example.bookshop.dto.UserDTO;
import com.example.bookshop.entity.UserMember;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

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

        log.info(userDTO);

       if (bindingResult.hasErrors()){
           log.info("회원가입 오류 : " + bindingResult.hasErrors());
           return "/user/register";
       }

        //비밀번호 일치 여부 확인
        if (!userDTO.getUserPw2().equals(userDTO.getUserPw())){
            bindingResult.reject("error2", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("error2","비밀번호가 일치하지 않습니다." );
            return "/user/register";
        }

       try {
           UserMember userMember = UserMember
                   .createUser(userDTO, passwordEncoder);
           userService.saveUser(userMember);
       } catch (IllegalStateException e) {
//           bindingResult.reject("error", e.getMessage());
           model.addAttribute("error3", e.getMessage());
           return "/user/register";
       }


        return "/main";
    }

    //회원정보 상세 확인
    @GetMapping ("/read")
    public String userRead(Principal principal, Model model, UserDTO userDTO) {
        //post 접근 불가로 get 변경해둠

        log.info("현재 로그인 회원 이름 : " + principal.getName());

        userDTO = userService.read(principal.getName());
        log.info("열람하는 user_name : " + userDTO);

        model.addAttribute("dto", userService.read(principal.getName()));

        return "/user/read";
    }

    //회원정보 수정 페이지 이동
    @GetMapping("/modify")
    public void userModify(Principal principal, Model model, UserDTO userDTO) {
        //post 접근 불가로 get 변경해둠

        model.addAttribute("dto", userService.read(principal.getName()));

    }

    //회원정보 수정
    @PostMapping("/modifypost")
    public String userModifyPost(@Valid UserDTO userDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Principal principal) {

        // 오류가 발생할 시 오류를 alert로 보기

        log.info("입력받은 userDTO : " + userDTO);

        if (bindingResult.hasErrors()){
            log.info("회원 수정 오류 : " + bindingResult.hasErrors());
            return "redirect:/user/modify";
        }

        //비밀번호 일치 여부 확인 // pw와 pw2가 같고 pw의 길이가 8이상 20이하일 때 통과
        if (userDTO.getUserPw2().equals(userDTO.getUserPw())
                && userDTO.getUserPw().length() >= 8
                && userDTO.getUserPw().length() <= 20) {

            userService.modify(userDTO);
            redirectAttributes.addFlashAttribute("result", "회원정보가 수정되었습니다.");

            return "/main";

        } else {

            bindingResult.reject("error2", "비밀번호가 일치하지 않습니다.");
            redirectAttributes.addFlashAttribute("result","비밀번호가 일치하지 않습니다." );

            return "redirect:/user/modify";
        }


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
