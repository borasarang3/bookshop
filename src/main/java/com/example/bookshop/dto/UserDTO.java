package com.example.bookshop.dto;

import com.example.bookshop.constant.Role;
import com.example.bookshop.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@ToString
public class UserDTO {

    @NotBlank(message = "아이디를 입력해주세요.")
    @Length(min=4, max=20, message = "아이디는 4자~20자여야 합니다.")
    private String user_id; //회원 Id

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Length(min = 8, max = 20, message = "비밀번호는 8자~20자여야 합니다.")
    private String user_pw; //비밀번호

    @NotBlank(message = "이름을 입력해주세요.")
    @Length(max = 10, message = "이름은 최대 10자까지 가능합니다.")
    private String user_name; //이름

    @NotBlank(message = "이메일을 입력해주세요.")
    private String email; //이메일

    private String address; //주소

    private Role role; //권한

    private static ModelMapper modelMapper = new ModelMapper();

    public static UserDTO of(User user){
        return modelMapper.map(user, UserDTO.class);
    }

}
