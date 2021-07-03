package com.wanbaep.membership.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApiResponseDto {
    private Boolean success;
    private Object response;
    private ErrorResponse error;
}
