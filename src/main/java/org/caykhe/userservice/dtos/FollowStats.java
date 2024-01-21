package org.caykhe.userservice.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FollowStats {
    private int followerCount;
    private int followingCount;
}
