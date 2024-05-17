package com.phytoncide.hikinglog.domain.member.config;

import com.phytoncide.hikinglog.domain.member.Service.MemberService;
import com.phytoncide.hikinglog.domain.member.jwt.JwtAuthenticationFilter;
import com.phytoncide.hikinglog.domain.member.jwt.JwtAuthorizationFilter;
import com.phytoncide.hikinglog.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private CorsConfig corsConfig;

    @Autowired
    private MemberService memberService;

    @Bean
    public static BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(Arrays.asList(authenticationProvider()));
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(memberService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), memberRepository))
                .addFilter(corsConfig.corsFilter());
        //csrf disable
        httpSecurity
                .csrf((auth) -> auth.disable());
//        httpSecurity
//                .exceptionHandling((exceptionHandler) -> exceptionHandler
//                .accessDeniedHandler(jwtAccessDeniedHandler)
//                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                );
        //From 로그인 방식 disable
        httpSecurity
                .formLogin((auth) -> auth.disable());
        //http basic 인증방식 disable
        httpSecurity
                .httpBasic((auth) -> auth.disable());
        //경로별 인가 작업
        httpSecurity
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/**").permitAll()
//                        .requestMatchers("/login", "/", "/join").permitAll()
//                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated());
        //세션 설정
        httpSecurity
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }
}
