package com.example.bookshop.controller;

import com.example.bookshop.constant.Role;
import com.example.bookshop.dto.CategoryDTO;
import com.example.bookshop.dto.ProductDTO;
import com.example.bookshop.dto.UserDTO;
import com.example.bookshop.entity.UserMember;
import com.example.bookshop.service.CategoryService;
import com.example.bookshop.service.ProductService;
import com.example.bookshop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    //나중에 세부적으로 막아보기
    //403 에러가 먼저 떠버림...

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final CategoryService categoryService;
    private final ProductService productService;

    //회원가입 페이지
    @GetMapping("/register")
    public String userRegister(Model model){

        UserDTO userDTO = new UserDTO();
        userDTO.setRole(Role.USER);

        model.addAttribute("userDTO", userDTO);

        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
        model.addAttribute("categoryList", categoryDTOList);

        return "/user/register";
    }

    //어드민용 회원가입 페이지
    @GetMapping("/adminregister")
    public String adminRegister(Model model){

        UserDTO userDTO = new UserDTO();
        userDTO.setRole(Role.ADMIN);

        model.addAttribute("userDTO", userDTO);

        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
        model.addAttribute("categoryList", categoryDTOList);

        return "/user/register";
    }

    //회원가입
    @PostMapping("/register")
    public String RegisterPost(@Valid UserDTO userDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
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

        redirectAttributes.addFlashAttribute("result", "회원가입이 완료되었습니다.");

        return "redirect:/main";
    }

    //회원정보 상세 확인
    @GetMapping ("/read")
    public String userRead(Principal principal, Model model,
                           UserDTO userDTO, RedirectAttributes redirectAttributes) {
        //post 접근 불가로 get 변경해둠
        //로그인한 사람이 회원정보의 사람과 같다면 or 권한이 관리자일 경우에만 열람 가능
        // 로그인한 사람의 권한을 찾아와야 함
        // 로그인 한 사람의 이름으로 정보를 찾고 권한 반환 > 비교하기

        log.info("현재 로그인 회원 이름 : " + principal.getName());

        UserDTO loginUser = userService.read(principal.getName());

        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
        model.addAttribute("categoryList", categoryDTOList);

        //기본
        // 로그인한 사람의 이름으로 정보를 찾아옴

        userDTO = userService.read(principal.getName());
        log.info("열람하는 user_name : " + userDTO);

        if (userDTO.getUserId().equals(principal.getName())
                || loginUser.getRole().name() == "ADMIN" ) {
            model.addAttribute("dto", userService.read(principal.getName()));

            return "/user/read";
        } else {

            redirectAttributes.addFlashAttribute("result", "열람 권한이 없습니다.");

            return "/main";
        }


    }

    @GetMapping("/findUser")
    public String find (UserDTO userDTO, Model model,
                      Principal principal, RedirectAttributes redirectAttributes){

        log.info("현재 로그인 회원 이름 : " + principal.getName());

        UserDTO loginUser = userService.read(principal.getName());

        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
        model.addAttribute("categoryList", categoryDTOList);

        if ( loginUser.getRole().name() == "ADMIN" ){
            return "/user/findUser";
        } else {

            redirectAttributes.addFlashAttribute("result", "열람 권한이 없습니다.");

            return "redirect:/main";
        }


    }

    @PostMapping("/findUser")
    public String findUser(UserDTO userDTO, Model model,
                           Principal principal, RedirectAttributes redirectAttributes){

        //로그인한 사람이 회원정보의 사람과 같다면 or 권한이 관리자일 경우에만 열람 가능

        log.info("현재 로그인 회원 이름 : " + principal.getName());

        UserDTO loginUser = userService.read(principal.getName());

        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
        model.addAttribute("categoryList", categoryDTOList);

        //해야 하는 것
        // 회원 목록에서 아이디를 눌렀을 때 아이디로 정보를 찾아옴
        // 목록에서 아이디를 누름(submit)
        // 컨트롤러에서 아이디를 받아서 서비스에서 아이디로 정보를 찾음
        // 찾은 정보를 열람

        log.info("받은 userDTO : " + userDTO);

        userDTO = userService.read(userDTO.getUserId());

        if (userDTO.getUserId().equals(principal.getName())
                || loginUser.getRole().name() == "ADMIN" ) {

            model.addAttribute("dto", userDTO);

            return "/user/read";

        } else {

            redirectAttributes.addFlashAttribute("result", "열람 권한이 없습니다.");

            return "redirect:/main";
        }

    }

    //회원정보 수정 페이지 이동
    @GetMapping("/modify")
    public String userModify(Principal principal, Model model, UserDTO userDTO,
                             RedirectAttributes redirectAttributes) {
        //post 접근 불가로 get 변경해둠
        //로그인한 사람이 회원정보의 사람과 같다면 or 권한이 관리자일 경우에만 수정 가능

        model.addAttribute("dto", userService.read(principal.getName()));

        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
        model.addAttribute("categoryList", categoryDTOList);

        return "/user/modify";

//        log.info("현재 로그인 회원 이름 : " + principal.getName());
//
//        UserDTO loginUser = userService.read(principal.getName());
//
//        if (userDTO.getUserId().equals(principal.getName())
//                || loginUser.getRole().name() == "ADMIN" ){
//
//            model.addAttribute("dto", userService.read(principal.getName()));
//
//            return "/user/modify";
//        } else {
//
//            redirectAttributes.addFlashAttribute("result", "열람 권한이 없습니다.");
//
//            return "redirect:/main";
//        }


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
            bindingResult.reject("error2", "회원 정보를 다시 확인해주세요.");
            redirectAttributes.addFlashAttribute("result", "회원 정보를 다시 확인해주세요.");
            return "redirect:/user/modify";
        }

        //비밀번호 일치 여부 확인 // pw와 pw2가 같고 pw의 길이가 8이상 20이하일 때 통과
        if (userDTO.getUserPw2().equals(userDTO.getUserPw())
                && userDTO.getUserPw().length() >= 8
                && userDTO.getUserPw().length() <= 20) {

            userService.modify(userDTO);
            redirectAttributes.addFlashAttribute("result", "회원정보가 수정되었습니다.");

            return "redirect:/main";

        } else {

            redirectAttributes.addFlashAttribute("result","비밀번호가 일치하지 않습니다." );

            return "redirect:/user/modify";
        }


    }

    //회원탈퇴
    @PostMapping("/remove")
    public String userRemove(UserDTO userDTO) {

        userService.remove(userDTO);

        return "redirect:/logout";

    }

    //회원목록
    @GetMapping("/list")
    public String userList(Model model, Principal principal, RedirectAttributes redirectAttributes) {
        //post 접근 불가로 get 변경해둠

        log.info("현재 로그인 회원 이름 : " + principal.getName());

        UserDTO loginUser = userService.read(principal.getName());

        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
        model.addAttribute("categoryList", categoryDTOList);

        if ( loginUser.getRole().name() == "ADMIN" ){

            model.addAttribute("userDTOList",  userService.allUserList() );

            return "/user/list";

        } else {

            redirectAttributes.addFlashAttribute("result", "열람 권한이 없습니다.");

            return "redirect:/main";
        }


    }

    //구매 이력 확인
    @GetMapping("/buy")
    public void userBuy(Model model) {
        //post 접근 불가로 get 변경해둠

        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
        model.addAttribute("categoryList", categoryDTOList);
    }

    //등록한 상품 확인
    @GetMapping("/product")
    public void userProduct(Principal principal, Model model) {
        //post 접근 불가로 get 변경해둠
        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
        model.addAttribute("categoryList", categoryDTOList);

        log.info("현재 로그인 회원 이름 : " + principal.getName());

        UserDTO loginUser = userService.read(principal.getName());

        model.addAttribute("loginUser", loginUser);

        List<ProductDTO> productDTOList = productService.userProduct(loginUser.getUserName());

        log.info(productDTOList);

        model.addAttribute("productDTOList", productDTOList);

    }

    //작성한 리뷰 확인
    @GetMapping("/review")
    public void userReview(Model model) {
        //post 접근 불가로 get 변경해둠

        List<CategoryDTO> categoryDTOList = categoryService.allCategoryList();
        model.addAttribute("categoryList", categoryDTOList);
    }








}
