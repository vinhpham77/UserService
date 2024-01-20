package org.caykhe.userservice.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VoteRequest {
    private Integer targetId;
    private Boolean targetType;
    private Boolean voteType;
    private String username;
}
