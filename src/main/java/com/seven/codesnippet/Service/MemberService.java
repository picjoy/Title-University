package com.seven.codesnippet.Service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seven.codesnippet.Controller.Dto.MemberResponseDto;
import com.seven.codesnippet.Controller.Dto.ResponseDto;
import com.seven.codesnippet.Domain.Member;
import com.seven.codesnippet.Jwt.TokenProvider;
import com.seven.codesnippet.Repository.MemberRepository;
import com.seven.codesnippet.Request.KakaoUserInfoDto;
import com.seven.codesnippet.Request.LoginRequestDto;
import com.seven.codesnippet.Request.MemberRequestDto;
import com.seven.codesnippet.Request.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

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
/*
    private String getKakaoAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "e7370c8ebb2446f385a95b0d4f737205");//내 api키
        body.add("redirect_uri", "http://localhost:8080/user/kakao/callback");
        body.add("code", code);//카카오로부터 받은 인가코드

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);//httpentity객체를 만들어서 보냄
        RestTemplate rt = new RestTemplate();//서버 대 서버 요청을 보냄
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );//리스폰스 받기

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();//바디부분
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);//json형태를 객체형태로 바꾸기
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();//서버 대 서버 요청을 보냄
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String username = jsonNode.get("properties")
                .get("nickname").asText();

        return new KakaoUserInfoDto(id, username);
    }

    public ResponseDto<?> kakaoLogin(String code, HttpServletResponse response) {
        try {
            // 1. "인가 코드"로 "액세스 토큰" 요청
            String accessToken = getKakaoAccessToken(code);
            // 2. 토큰으로 카카오 API 호출
            KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

            // DB 에 중복된 Kakao Id 가 있는지 확인
            Long kakaoId = kakaoUserInfo.getKakaoId();
            Member kakaoUser = memberRepository.findByKakaoId(kakaoId);
            if(kakaoUser != null){
                TokenDto tokenDto = tokenProvider.generateTokenDto(kakaoUser);
                tokenToHeaders(tokenDto, response);
                return ResponseDto.success(
                        MemberResponseDto.builder()
                                .id(kakaoUser.getKakaoId())
                                .email(kakaoUser.getEmail())
                                .nickname(kakaoUser.getNickname())
                                .build()
                );
            }
            else{
                // 회원가입
                Member member = Member.builder()
                        .email(UUID.randomUUID().toString())
                        .nickname(kakaoUserInfo.getUsername())
                        .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                        .kakaoId(kakaoId)
                        .build();
                memberRepository.save(member);
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
        }
        catch (JsonProcessingException e){
            return ResponseDto.fail("403","로그인에 실패했습니다.");
        }
    }*/

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
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
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
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
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


  }
