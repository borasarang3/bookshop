package com.example.bookshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.AuthorizeRequestsDsl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
                (AuthorizeRequests) -> AuthorizeRequests
                        .requestMatchers("/user/list").hasRole("ADMIN")
                        .requestMatchers("/user/findUser").hasRole("ADMIN")
                        .requestMatchers("/user/register").permitAll()
                        .requestMatchers("/user/adminregister").permitAll()
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/product/list").permitAll()
                        .requestMatchers("/product/read/**").permitAll()
                        .requestMatchers("/product/**").authenticated()
                        .requestMatchers("css/**", "/js/**", "/**").permitAll()
                        .requestMatchers("/**").permitAll()
        )
                .csrf((csrf) -> csrf.disable())

                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .usernameParameter("userId")
                        .failureUrl("/login/error")
                )
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true))

                // 로그인이 되지 않은 사용자가 로그인을 요하는 페이지 접속시 (rest)
                .exceptionHandling(
                        a -> a.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                );

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //  스프링 시큐리티의 인증 처리 , 인증은 로그인
    // AuthenticationManager는 사용자 인증시 앞에서 작성한 UserSecurityService 와 passwordEncoder를
    //내부적으로 사용 인증과 권한 부여
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/css/**", "/js/**", "/img/**");
    }



}
