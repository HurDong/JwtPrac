package com.hurgumti.jwtprac.service;

import com.hurgumti.jwtprac.entity.User;
import com.hurgumti.jwtprac.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // username으로 유저를 찾고, UserDetails로 래핑해서 반환
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다: " + username));
        return org.springframework.security.core.userdetails.User.builder().username(user.getUsername()).password(user.getPassword())  // 꼭 인코딩된 패스워드여야 함
                .roles(user.getRole().replace("ROLE_", ""))  // ROLE_USER → USER
                .build();
    }
}

