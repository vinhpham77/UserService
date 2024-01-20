package org.caykhe.userservice.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookmarkDetailRequest {
    private Integer targetId;
    private Boolean type;
}
