package org.caykhe.userservice.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.caykhe.userservice.dtos.JsonWebToken;
import org.caykhe.userservice.dtos.SignInRequest;
import org.caykhe.userservice.dtos.SignUpRequest;
import org.caykhe.userservice.models.User;
import org.caykhe.userservice.services.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody SignInRequest signin) {
        return new ResponseEntity<>(authenticationService.signIn(signin), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest signup) {
        User newUser = authenticationService.signUp(signup);
        if (newUser != null) {
            return new ResponseEntity<>(newUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Đăng ký không thành công", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Chưa cung cấp refresh token");
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh_token")) {
                String refreshToken = cookie.getValue();
                authenticationService.logout(refreshToken);
                return ResponseEntity.ok("Đăng xuất thành công!");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Chưa cung cấp refresh token");
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<?> getRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Chưa cung cấp refresh token");
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh_token")) {
                String refreshToken = cookie.getValue();
                JsonWebToken response = authenticationService.refreshToken(refreshToken);
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Chưa cung cấp refresh token");
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(user);
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String refreshToken) {
        authenticationService.logout(refreshToken);
        return ResponseEntity.ok("Đăng xuất thành công!");
    }
}
