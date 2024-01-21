package org.caykhe.userservice.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Data
public class UserDto {
    @NotBlank(message = "Email không được để trống")
    private String email;

    private Boolean gender;
    private Date birthdate;
    private String avatarUrl;
    private String bio;

    @NotBlank(message = "Tên hiển thị không được để trống")
    private String displayName;
}
