package com.seven.codesnippet.Service;


import com.seven.codesnippet.Controller.Dto.MemberResponseDto;
import com.seven.codesnippet.Controller.Dto.NicknameResponseDto;
import com.seven.codesnippet.Controller.Dto.ResponseDto;
import com.seven.codesnippet.Domain.Member;
import com.seven.codesnippet.Jwt.TokenProvider;
import com.seven.codesnippet.Repository.MemberRepository;
import com.seven.codesnippet.Request.LoginRequestDto;
import com.seven.codesnippet.Request.MemberRequestDto;
import com.seven.codesnippet.Request.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;

    public ResponseDto<?> createMember(MemberRequestDto requestDto) {
        if (null != isPresentEmail(requestDto.getEmail())) {
            return ResponseDto.fail("DUPLICATED_EMAIL",
                    "중복된 이메일 입니다.");
        }

        if (null != isPresentNickName(requestDto.getNickname())) {
            return ResponseDto.fail("DUPLICATED_NICKNAME",
                    "중복된 닉네임 입니다.");
        }

        if (!requestDto.getPassword().equals(requestDto.getPasswordCheck())) {
            return ResponseDto.fail("PASSWORDS_NOT_MATCHED",
                    "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        Member member = Member.builder()
                .email(requestDto.getEmail())
                .nickname(requestDto.getNickname())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();
        memberRepository.save(member);
        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .email(member.getEmail())
                        .nickname(member.getNickname())
                        .build()

        );

    }

    public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response) {
        Member member = isPresentEmail(requestDto.getEmail());
        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }

        if (!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
            return ResponseDto.fail("INVALID_MEMBER", "사용자를 찾을 수 없습니다.");
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response);

        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .email(member.getEmail())
                        .nickname(member.getNickname())
                        .build()
        );

    }


    public ResponseDto<?> logout(HttpServletRequest request) {
        System.out.println(request.getHeader("refreshtoken"));
        System.out.println(request.getHeader("refreshToken"));
        System.out.println(request.getHeader("RefreshToken"));
        System.out.println(request.getHeader("Refreshtoken"));
        if (!tokenProvider.validateToken(request.getHeader("refreshtoken"))) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }

        return tokenProvider.deleteRefreshToken(member);
    }

    @Transactional(readOnly = true)
    public Member isPresentEmail(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        return optionalMember.orElse(null);
    }

    @Transactional(readOnly = true)
    public Member isPresentNickName(String nickname) {
        Optional<Member> optionalMember = memberRepository.findByNickname(nickname);
        return optionalMember.orElse(null);
    }

    private void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("RefreshToken", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }

    // 이메일 중복 체크
    // 중복되는 경우 true, 중복되지 않은경우 false가 리턴
    public boolean checkEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    // 닉네임 중복 체크
    public boolean checkNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    //    닉네임 가져오기
    public NicknameResponseDto info() {
        Member member = memberRepository.getReferenceById(1L);
        return NicknameResponseDto.builder()
                        .nickname(member.getNickname())
                        .build();
    }
}
