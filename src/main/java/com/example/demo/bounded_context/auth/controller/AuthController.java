package com.example.demo.bounded_context.auth.controller;

import com.example.demo.bounded_context.auth.dto.SignInUserRequest;
import com.example.demo.bounded_context.auth.dto.SignUpUserRequest;
import com.example.demo.bounded_context.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody SignUpUserRequest request) {
        try{
            authService.signUp(request);
            return ResponseEntity.ok()
                    .body("회원가입에 성공했습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회원가입에 실패했습니다. " + e.getMessage());
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity signIn(@RequestBody SignInUserRequest request) {
        try{
            return ResponseEntity.ok().body(authService.signIn(request));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("로그인에 실패했습니다. " + e.getMessage());
        }
    }
}