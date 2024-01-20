package org.caykhe.userservice.services;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.caykhe.userservice.dtos.*;
import org.caykhe.userservice.models.Authentication;
import org.caykhe.userservice.models.User;
import org.caykhe.userservice.repositories.AuthenticationRepository;
import org.caykhe.userservice.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationRepository authenticationRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    final EntityManager entityManager;

    public boolean checkUserByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public User signUp(SignUpRequest request) {
        if (checkUserByEmail(request.getEmail())) {
            throw new ApiException("Email đã tồn tại", HttpStatus.BAD_REQUEST);
        }
        if (checkUser(request.getUsername())) {
            throw new ApiException("Username đã tồn tại", HttpStatus.BAD_REQUEST);
        }
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .displayName(request.getDisplayName())
                .role(Role.ROLE_member)
                .birthdate(null)
                .bio(null)
                .gender(null).build();
        return userRepository.save(user);
    }

    public JsonWebToken signIn(SignInRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ApiException("Tài khoản hoặc mật khẩu không hợp lệ.", HttpStatus.UNAUTHORIZED));
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (Exception e) {
            throw new ApiException(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }


        var authentication = authenticationRepository.findByUsername(user);
        if (authentication.isPresent()) {
            var refreshToken = authentication.get().getRefreshToken();
            if (!jwtService.isTokenExpired(refreshToken, true)) {
                return JsonWebToken.builder().token(refreshToken).build();
            }
        }

        var refreshToken = jwtService.generateToken(user, true);
        var newAuthentication = Authentication.builder()
                .username(user)
                .refreshToken(refreshToken)
                .build();

        updateOrCreateIfNot(newAuthentication);
        return JsonWebToken.builder().token(refreshToken).build();
    }

    public void updateOrCreateIfNot(Authentication newAuthentication) {
        Optional<Authentication> authentication = authenticationRepository.findByUsername(newAuthentication.getUsername());
        if (authentication.isEmpty()) {
            authenticationRepository.save(newAuthentication);
        } else {
            var existingAuthentication = authentication.get();
            existingAuthentication.setRefreshToken(newAuthentication.getRefreshToken());
            authenticationRepository.save(existingAuthentication);
        }
    }

    boolean checkUser(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public JsonWebToken refreshToken(String refreshToken) {
        var recordAuthUser = getAuthenticationAndUser(refreshToken);
        var user = recordAuthUser.user();
        var authentication = recordAuthUser.authentication();

        if (jwtService.isTokenExpired(authentication.getRefreshToken(), true))
            throw new ApiException("Phiên truy cập đã hết hạn. Vui lòng đăng nhập lại!", HttpStatus.PRECONDITION_FAILED);

        var claims = new HashMap<String, Object>();
        claims.put("role", user.getRole());
        claims.put("displayName", user.getDisplayName());

        var accessToken = jwtService.generateToken(claims, user, false);
        return JsonWebToken.builder().token(accessToken).build();
    }

    private AuthUser getAuthenticationAndUser(String refreshToken) {
        String username = jwtService.extractUserName(refreshToken, true);
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("Có lỗi xảy ra. Vui lòng đăng nhập lại!", HttpStatus.NOT_ACCEPTABLE));
        var authentication = authenticationRepository.findByUsername(user)
                .orElseThrow(() -> new ApiException("Có lỗi xảy ra. Vui lòng đăng nhập lại!", HttpStatus.NOT_ACCEPTABLE));
        if (!authentication.getRefreshToken().equals(refreshToken))
            throw new ApiException("Có lỗi xảy ra. Vui lòng đăng nhập lại!", HttpStatus.NOT_ACCEPTABLE);

        return AuthUser.builder().user(user).authentication(authentication).build();
    }

    public void logout(String refreshToken) {
         var user = getAuthenticationAndUser(refreshToken).user();
        User managedFollower = entityManager.merge(user);
        authenticationRepository.deleteByUsername(user);
        System.out.println("oke");
    }
    private User getAuthenticationAndUser1(String refreshToken) {
        String username = jwtService.extractUserName(refreshToken, true);
        System.out.println(username);
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("Có lỗi xảy ra. Vui lòng đăng nhập lại!", HttpStatus.NOT_ACCEPTABLE));
        var authentication = authenticationRepository.findByUsername(user)
                .orElseThrow(() -> new ApiException("Có lỗi xảy ra. Vui lòng đăng nhập lại!", HttpStatus.NOT_ACCEPTABLE));
        if (!authentication.getRefreshToken().equals(refreshToken))
            throw new ApiException("Có lỗi xảy ra. Vui lòng đăng nhập lại!", HttpStatus.NOT_ACCEPTABLE);

        return user;
    }
}
