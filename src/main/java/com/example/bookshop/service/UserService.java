package com.example.bookshop.service;

import com.example.bookshop.constant.Role;
import com.example.bookshop.entity.User;
import com.example.bookshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    //회원가입
    public User saveUser (User user){

        log.info("등록된 사용자가 있는지 확인합니다.");

        validateDuplicateUser(user);

        log.info("신규회원임을 확인했습니다.");

        return userRepository.save(user);

    }

    //중복회원 검사
    private void validateDuplicateUser(User user) {

        log.info("이미 가입된 회원인지 확인합니다.");

        //이미 저장된 아이디인지 확인하여 중복 회원을 확인
        User findIdUser = userRepository.findByUser_id(user.getUser_id());

        //이미 가입된 id라면
        if (findIdUser != null){
            log.info("이미 가입한 회원입니다.");
            throw new IllegalStateException("이미 가입한 회원입니다.");
        }

        //이미 저장된 이메일인지 확인하여 중복 회원을 확인
        User findUser = userRepository.findByEmail(user.getEmail());

        //이미 가입된 email이라면
        if (findUser != null){
            log.info("이미 가입한 회원입니다.");
            throw new IllegalStateException("이미 가입한 회원입니다.");
        }

    }


    @Override
    public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException {

        User user = this.userRepository.findByUser_id(user_id);

        if (user == null){
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다." + user_id);
        }

        log.info("현재 로그인 상태 : " + user);
        log.info("현재 로그인 권한 : " + user.getRole().name());

        String role = "";

        if("ADMIN".equals(user.getRole().name())){   //auth 컬럼을 추가로 지정해서 사용
            log.info("관리자");
            role = Role.ADMIN.name();
//            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.name()));

        }else {
            log.info("일반 회원");
            role = Role.USER.name();
//            authorities.add(new SimpleGrantedAuthority(Role.USER.name()));
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUser_id())
                .password(user.getUser_pw())
                .roles(role)
                .build();
    }




}
