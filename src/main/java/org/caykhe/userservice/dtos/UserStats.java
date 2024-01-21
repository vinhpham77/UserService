package org.caykhe.userservice.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStats {
    private int id;
    private String username;
    private String email;
    private String displayName;
    private Role role;
    private String avatarUrl;
    private int followingCount;
    private int followerCount;
}
