package com.hurgumti.jwtprac.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).headers(headers -> headers.addHeaderWriter(new org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter(org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))).authorizeHttpRequests(auth -> auth.requestMatchers("/h2-console/**").permitAll().anyRequest().authenticated()).httpBasic(httpBasic -> {
                }) // 또는 .formLogin(form -> {})로 명시적 대체
                .logout(logout -> logout.logoutSuccessUrl("/")); // 선택사항

        return http.build();
    }
}

