package com.hurgumti.jwtprac.controller;

import com.hurgumti.jwtprac.dto.LoginRequest;
import com.hurgumti.jwtprac.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        Logger logger = LoggerFactory.getLogger(AuthController.class);

        logger.info("로그인 요청: username={}", request.getUsername());

        // 사용자 인증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // 인증된 사용자 정보 가져오기
        String username = authentication.getName();
        logger.info("인증 성공: username={}", username);

        // JWT 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(username);
        String refreshToken = jwtUtil.generateRefreshToken(username);
        logger.info("JWT 토큰 생성: accessToken={}, refreshToken={}", accessToken, refreshToken);

        // 리프레시 토큰을 HttpOnly 쿠키에 저장
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true); // HTTPS에서만 전송
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7일
        response.addCookie(refreshTokenCookie);

        // 엑세스 토큰 반환
        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken
        ));
    }

   @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        // 리프레시 토큰 쿠키 초기화
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true); // HTTPS에서만 전송
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0); // 쿠키 즉시 만료
        response.addCookie(refreshTokenCookie);

        // HTTP 리다이렉트 응답
        return ResponseEntity.status(302)
                .header("Location", "/login.html")
                .build();
    }


    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        System.out.println("✅ 현재 로그인된 사용자: " + username);

        return ResponseEntity.ok(Map.of(
                "message", "현재 로그인한 유저는 " + username + "입니다."
        ));
    }


}
