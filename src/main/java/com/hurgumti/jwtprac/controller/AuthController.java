package com.hurgumti.jwtprac.controller;

import com.hurgumti.jwtprac.dto.LoginRequest;
import com.hurgumti.jwtprac.util.JwtUtil;
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
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String accessToken = jwtUtil.generateAccessToken(request.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(request.getUsername());

        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        ));
    }

    //    @GetMapping("/me")
//    public ResponseEntity<?> getMyInfo(@RequestHeader("Authorization") String authHeader) {
//        // "Bearer ..." 문자열에서 토큰만 추출
//        String token = authHeader.replace("Bearer ", "");
//
//        // JWT에서 username 추출
//        String username = jwtUtil.extractUsername(token);
//
//        // 콘솔 출력
//        System.out.println("✅ 현재 로그인된 사용자: " + username);
//
//        return ResponseEntity.ok(Map.of(
//                "message", "현재 로그인한 유저는 " + username + "입니다."
//        ));
//    }
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        System.out.println("✅ 현재 로그인된 사용자: " + username);

        return ResponseEntity.ok(Map.of(
                "message", "현재 로그인한 유저는 " + username + "입니다."
        ));
    }


}
