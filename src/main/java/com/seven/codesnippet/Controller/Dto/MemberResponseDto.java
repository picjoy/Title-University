package com.seven.codesnippet.Controller.Dto;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String email;
    private String nickname;
}
