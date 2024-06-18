package com.example.bookshop.entity;

import com.example.bookshop.constant.Role;
import com.example.bookshop.dto.UserDTO;
import com.example.bookshop.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name = "userMember")
public class UserMember extends BaseEntity {

    @Id
    @Column(name = "user_id", nullable = false, length = 20)
    private String userId; //회원 Id

    @Column(nullable = false)
    private String userPw; //비밀번호

    @Column(nullable = false, length = 10)
    private String userName; //이름

    @Column(unique = true, nullable = false)
    private String email; //이메일

    private String address; //주소

    @Enumerated(EnumType.STRING)
    private Role role; //권한

    //회원가입용
    public static UserMember createUser(UserDTO userDTO,
                                        PasswordEncoder passwordEncoder){

        //modelmapper
        UserMember userMember = new UserMember();
        userMember.setUserId(userDTO.getUserId());

        //비밀번호 암호화
        String password = passwordEncoder.encode(userDTO.getUserPw());
        userMember.setUserPw(password);

        userMember.setUserName(userDTO.getUserName());
        userMember.setEmail(userDTO.getEmail());
        userMember.setAddress(userDTO.getAddress());

        //어떤 회원가입 페이지를 통해서 왔는지를 통해서
        //권한을 다르게 가져가기
        userMember.setRole(userDTO.getRole());

        return userMember;
    }

    private static ModelMapper modelMapper = new ModelMapper();

    public static UserMember of(UserDTO userDTO){
        return modelMapper.map(userDTO, UserMember.class);
    }




}
