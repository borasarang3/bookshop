package com.example.bookshop.controller;

import com.example.bookshop.dto.CategoryDTO;
import com.example.bookshop.dto.UserDTO;
import com.example.bookshop.entity.UserMember;
import com.example.bookshop.service.CategoryService;
import com.example.bookshop.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/login")
public class LoginController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final CategoryService categoryService;

    @GetMapping("")
    public String loginGet(Model model){

        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
        model.addAttribute("categoryList", categoryDTOList);

        return "/login";
    }

    @GetMapping("/error")
    public String loginError(Model model,
                             RedirectAttributes redirectAttributes){

        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
        model.addAttribute("categoryList", categoryDTOList);

        redirectAttributes.addFlashAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요" );
        return "redirect:/login";
    }

    @GetMapping("/findID")
    public void findIDGet(Model model){
        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
        model.addAttribute("categoryList", categoryDTOList);
    }

    @PostMapping("/findID")
    public String findIDPost(String userName, String email,
                           Model model){

            log.info("받은 이름과 이메일 : " + userName + " / " + email);

        UserDTO userDTO = userService.findId(userName, email);

        log.info("회원 찾기 기능 처리후 userDTO : " + userDTO);

        if (userDTO == null){
            model.addAttribute("result", "등록된 회원이 존재하지 않습니다.");
            return "/login/findID";
        } else {

            model.addAttribute("result", "회원님의 아이디는 " + userDTO.getUserId()  + " 입니다.");

            return "/login/findID";
        }

    }

    @GetMapping("/findPW")
    public void findPWGet(Model model){
        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
        model.addAttribute("categoryList", categoryDTOList);
    }

    @ResponseBody
    @PostMapping("/findPW")
    public String findPWPost(@RequestBody UserDTO userDTO){

        //1. 이름, 이메일, 아이디를 입력받는다.
        log.info("받은 UserDTO : " + userDTO);

        //2. 입력받은 정보로 회원이 있는지 확인한다.
       UserDTO userDTO1 = userService.checkUser(userDTO.getUserName(), userDTO.getEmail(), userDTO.getUserId());

        //3-1. 회원이 없다면 오류 메시지를 출력한다. (종료) //여기까지는 할 수 있을 거 같음

        if (userDTO1 == null){
           // model.addAttribute("result", "등록된 회원이 존재하지 않습니다.");
            return "aaa";
        }

        return "bbb";

        //3-2. 회원이 있다면 '같은 페이지'에서 다시 비밀번호를 입력받는다.
        //4-1. 두 비밀번호가 같고 길이가 8~20자라면 변경. 완료 메시지 출력.
        //4-2. 비밀번호 조건이 충족되지 않으면 오류 메시지 출력.

    }

    @ResponseBody
    @PostMapping("/changePw")
    public String changePw (@RequestBody UserDTO userDTO){

        log.info("받은 UserDTO : " + userDTO);

        //비밀번호 일치 여부 확인
        if (userDTO.getUserPw2().equals(userDTO.getUserPw())
                && userDTO.getUserPw().length() >= 8
                && userDTO.getUserPw().length() <= 20){

            userService.changePw(userDTO); // 비밀번호 변경

            return "bbb";
        }



        return "aaa";



    }





}
