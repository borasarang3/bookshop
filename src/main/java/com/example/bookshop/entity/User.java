package com.example.bookshop.entity;

import com.example.bookshop.constant.Role;
import com.example.bookshop.dto.UserDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@Table(name = "user")
public class User {

    @Id
    @Column(name = "user_id", nullable = false, length = 20)
    private String user_id; //회원 Id

    @Column(nullable = false)
    private String user_pw; //비밀번호

    @Column(nullable = false, length = 10)
    private String user_name; //이름

    @Column(unique = true, nullable = false)
    private String email; //이메일

    private String address; //주소

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regidate; //회원가입일

    @LastModifiedDate
    private LocalDateTime modidate; //회원정보 수정일

    @Enumerated(EnumType.STRING)
    private Role role; //권한

    //회원가입용
    public static User createUser(UserDTO userDTO,
                                  PasswordEncoder passwordEncoder){

        //modelmapper
        User user = new User();
        user.setUser_id(userDTO.getUser_id());

        //비밀번호 암호화
        String password = passwordEncoder.encode(userDTO.getUser_pw());
        user.setUser_pw(password);

        user.setUser_name(userDTO.getUser_name());
        user.setEmail(userDTO.getEmail());
        user.setAddress(userDTO.getAddress());

        //어떤 회원가입 페이지를 통해서 왔는지를 통해서
        //권한을 다르게 가져가기
        user.setRole(userDTO.getRole());

        return user;
    }



}
