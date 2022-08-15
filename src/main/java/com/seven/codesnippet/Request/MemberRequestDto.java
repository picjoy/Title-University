package com.seven.codesnippet.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {
    /*
    1.. `@NotBlank` : 닉네임 입력폼이 비어있는 상태로 요청을 보내면 해당 에러메시지가 나타납니다.
    2. `@Size` : 지정된 사이즈에 벗어나는 값 입력시 해당 에러메시지가 나타납니다.
    3. `@Email` : 이메일 형태만 입력이 가능합니다.
    4. `@Pattern` : 지정된 패턴만 입력하게 하여 휴대폰 번호 폼에서 이상한 값들이 요청되는 것을 방지합니다.
*/
    @NotBlank(message = "이메일 주소를 입력해주세요.")
    @Email(message = "올바른 이메일 주소를 입력해주세요.")
    private String email;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하로 입력해주세요.")
    private String nickname;


    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 4, max = 32, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    @Pattern(regexp = "[a-z\\d]*${3,32}")
    private String password;

    @NotBlank
    private String passwordConfirm;


}
