package com.example.bookshop.repository;

import com.example.bookshop.entity.UserMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserMember, String> {

    //아이디로 정보 찾기
    @Query("select u from UserMember u where u.userId = :userId")
    UserMember findByUserId(String userId);

    //이메일로 정보 찾기
    UserMember findByEmail(String email);

    @Query("select u from UserMember u where u.userName = :userName")
    UserMember findByUserName (String userName);

    //이름과 이메일로 아이디 찾기
    UserMember findByUserNameAndEmail(String userName, String email);

    //이름과 이메일과 아이디로 회원 찾기
    UserMember findByUserNameAndEmailAndUserId(String userName, String email, String userId);

}
