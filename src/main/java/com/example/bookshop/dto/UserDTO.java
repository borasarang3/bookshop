package com.example.bookshop.dto;

import com.example.bookshop.constant.Role;
import com.example.bookshop.entity.UserMember;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class UserDTO {

    @NotBlank(message = "아이디를 입력해주세요.")
    @Length(min=4, max=20, message = "아이디는 4자~20자여야 합니다.")
    private String userId; //회원 Id

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Length(min = 8, max = 20, message = "비밀번호는 8자~20자여야 합니다.")
    private String userPw; //비밀번호

    @NotBlank(message = "비밀번호를 다시 한번 입력해주세요.")
    @Length(min = 8, max = 20, message = "비밀번호는 8자~20자여야 합니다.")
    @Transient
    private String userPw2; //비밀번호 확인용

    @NotBlank(message = "이름을 입력해주세요.")
    @Length(max = 10, message = "이름은 최대 10자까지 가능합니다.")
    private String userName; //이름

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email
    private String email; //이메일

    private String address; //주소

    private Role role; //권한

    private LocalDateTime regidate; //회원가입일

    private LocalDateTime modidate; //회원정보 수정일

    private static ModelMapper modelMapper = new ModelMapper();

    public static UserDTO of(UserMember userMember){
        return modelMapper.map(userMember, UserDTO.class);
    }

}





