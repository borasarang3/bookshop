package com.example.bookshop.service;

import com.example.bookshop.constant.Role;
import com.example.bookshop.dto.UserDTO;
import com.example.bookshop.entity.UserMember;
import com.example.bookshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    //회원가입
    public UserMember saveUser (UserMember userMember){

        log.info("등록된 사용자가 있는지 확인합니다.");

        validateDuplicateUser(userMember);

        log.info("신규회원임을 확인했습니다.");

        return userRepository.save(userMember);

    }

    //중복회원 검사
    private void validateDuplicateUser(UserMember userMember) {

        log.info("이미 가입된 회원인지 확인합니다.");


        //이미 저장된 아이디인지 확인하여 중복 회원을 확인
        UserMember findIdUserMember = userRepository.findByUserId(userMember.getUserId());

        //이미 가입된 id라면
        if (findIdUserMember != null){
            log.info("이미 가입한 회원입니다.");
            throw new IllegalStateException("이미 가입한 회원입니다.");
        }

        //이미 저장된 이메일인지 확인하여 중복 회원을 확인
        UserMember findUserMember = userRepository.findByEmail(userMember.getEmail());

        //이미 가입된 email이라면
        if (findUserMember != null){
            log.info("이미 가입한 회원입니다.");
            throw new IllegalStateException("이미 가입한 회원입니다.");
        }

    }


    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        log.info("진입정보" + userId);

        UserMember userMember = this.userRepository.findByUserId(userId);
        log.info(userMember);

        if (userMember == null){
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다." + userId);
        }

        log.info("현재 로그인 상태 : " + userMember);
        log.info("현재 로그인 권한 : " + userMember.getRole().name());

        String role = "";

        if("ADMIN".equals(userMember.getRole().name())){   //auth 컬럼을 추가로 지정해서 사용
            log.info("관리자");
            role = Role.ADMIN.name();
//            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.name()));

        }else {
            log.info("일반 회원");
            role = Role.USER.name();
//            authorities.add(new SimpleGrantedAuthority(Role.USER.name()));
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(userMember.getUserId())
                .password(userMember.getUserPw())
                .roles(role)
                .build();
    }

    //회원정보 읽기
    public UserDTO read(String userName){

        UserMember userMember = userRepository.findByUserId(userName);

        if (userMember == null){
            log.info("null임");
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");

        }

        log.info(userMember);

        UserDTO userDTO = modelMapper.map(userMember, UserDTO.class);

        return userDTO;


    }

    //회원정보 수정
    public UserDTO modify(UserDTO userDTO){

        //dto에서 가져온 값을 엔티티에 저장
        //컨트롤러에서 보낸 dto 값이 userDTO에 있음

        log.info("service에서 받은 DTO 확인 : " + userDTO);

        log.info("passwordEncoder 적용 전 : " + userDTO.getUserPw());
        userDTO.setUserPw(passwordEncoder.encode(userDTO.getUserPw()));
        log.info("passwordEncoder 적용 후 : " + userDTO.getUserPw());

        log.info("userDTO 확인 : " + userDTO);

        UserMember userMember = UserMember.of(userDTO);

        log.info("userMember 확인 : " + userMember);

        userRepository.save(userMember); //entity 저장

//        UserMember userMember = userRepository.findByUserId(userDTO.getUserId());
//
//        log.info("passwordEncoder 적용 전 : " + userMember.getUserPw());
//        userMember.setUserPw(passwordEncoder.encode(userMember.getUserPw()));
//        log.info("passwordEncoder 적용 후 : " + userMember.getUserPw());
//
//        log.info("userMember 확인 : " + userMember);

//        userRepository.save(userMember); //entity 저장

        return userDTO;

    }

    // 회원 탈퇴
    public void remove(UserDTO userDTO) {

        userRepository.deleteById(userDTO.getUserId());

    }

    // 회원 목록
    public List<UserDTO> allUserList(){

        List<UserMember> userMemberList = userRepository.findAll();

        List<UserDTO> userDTOList = userMemberList.stream()
                .map(userMember -> modelMapper.map(userMember, UserDTO.class))
                .collect(Collectors.toList());

        userDTOList.forEach(userDTO -> log.info(userDTO));

        return userDTOList;

    }


    //아이디 찾기
    public UserDTO findId(String userName, String email) {

        UserMember userMember = userRepository.findByUserNameAndEmail(userName, email);

        if(userMember == null){
            //throw로 보낼시 모든 메서드를 중단하고 바로 예외를 내보내기 때문에
            //값이 컨트롤러로 가지 않음 // return과는 달라짐
            //userDTO를 새로 만들면 그 안의 값은 null 값이나 생성자가 있기 때문에 null이 아님
            //null로 보내려면 return null을 해야 함
            return null;

        }

        UserDTO userDTO = UserDTO.of(userMember);

        return userDTO;

    }

    //유저 찾기
    public UserDTO checkUser(String userName, String email, String userId){
        UserMember userMember =
                userRepository.findByUserNameAndEmailAndUserId(userName, email, userId);

        if (userMember == null){
            return null;
        }

        UserDTO userDTO = UserDTO.of(userMember);

        return userDTO;

    }

    public void changePw (UserDTO userDTO){

        log.info("service에서 받은 DTO 확인 : " + userDTO);

        log.info("passwordEncoder 적용 전 : " + userDTO.getUserPw());
        userDTO.setUserPw(passwordEncoder.encode(userDTO.getUserPw()));
        log.info("passwordEncoder 적용 후 : " + userDTO.getUserPw());

        log.info("DTO 확인 : " + userDTO);

        UserMember userMember =
                userRepository.
                        findByUserNameAndEmailAndUserId(userDTO.getUserName(),
                                userDTO.getEmail(),
                                userDTO.getUserId());

        userMember.setUserPw(userDTO.getUserPw());

        log.info("userMember 확인 : " + userMember);

        userRepository.save(userMember);

    }

}
