package org.caykhe.userservice.dtos;

import lombok.Data;

@Data
public class RequestRessetPass {
private String username;
private String newPassword;
private String otp;

}
