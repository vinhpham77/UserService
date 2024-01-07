package org.caykhe.userservice.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignInRequest {
    @NotBlank(message = "Tài khoản không được để trống")
    String username;
    @NotBlank(message = "Mật khẩu không được để trống")
    String password;
}
