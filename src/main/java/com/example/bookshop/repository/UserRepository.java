package com.example.bookshop.repository;

import com.example.bookshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, String> {

    //아이디로 정보 찾기
    @Query("select u from User u where u.user_id = :user_id")
    User findByUser_id(String user_id);

    //이메일로 정보 찾기
    User findByEmail(String email);


}
