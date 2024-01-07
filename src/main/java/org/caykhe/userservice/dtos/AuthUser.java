package org.caykhe.userservice.dtos;

import lombok.Builder;
import org.caykhe.userservice.models.Authentication;
import org.caykhe.userservice.models.User;

@Builder
public record AuthUser(Authentication authentication, User user) {
}