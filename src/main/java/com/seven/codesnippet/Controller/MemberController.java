package com.seven.codesnippet.Controller;

import com.seven.codesnippet.Controller.Dto.ResponseDto;
import com.seven.codesnippet.Request.LoginRequestDto;
import com.seven.codesnippet.Request.MemberRequestDto;
import com.seven.codesnippet.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    @Autowired  // 각 상황의 타입에 맞는 IoC컨테이너 안에 존재하는 Bean을 자동으로 주입
    private final MemberService memberService;

    @PostMapping("/api/member/signup")  // @Valid를 이용하면, service 단이 아닌 객체 안에서, 들어오는 값에 대해 검증
    private ResponseDto<?> signup(@RequestBody @Valid MemberRequestDto requestDto){
        return memberService.createMember(requestDto);
    }

    @PostMapping("/api/member/login")
    public ResponseDto<?> login(@RequestBody @Valid LoginRequestDto requestDto, HttpServletResponse response){
        return memberService.login(requestDto, response);
    }

    @PostMapping("/api/member/logout")
    public ResponseDto<?> logout(HttpServletRequest request){
        return memberService.logout(request);
    }

    @GetMapping("/api/member/idCheck/{email}")
    public ResponseEntity<Boolean> checkEmail(@PathVariable String email){
        return ResponseEntity.ok(memberService.checkEmail(email));
    }

    @GetMapping("/api/member/nicknameCheck/{nickname}")
    public ResponseEntity<Boolean> checkNickname(@PathVariable String nickname){
        return ResponseEntity.ok(memberService.checkNickname(nickname));
    }


}
