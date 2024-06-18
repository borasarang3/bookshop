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


}
