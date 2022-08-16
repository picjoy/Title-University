package com.seven.codesnippet.Controller;

import com.seven.codesnippet.Controller.Dto.ResponseDto;
import com.seven.codesnippet.Domain.Member;
import com.seven.codesnippet.Domain.TitlePost;
import com.seven.codesnippet.Repository.MemberRepository;
import com.seven.codesnippet.Repository.TitlePostRepository;
import com.seven.codesnippet.Service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TitlePostController {

    private final MainService mainService;
    private final TitlePostRepository titlePostRepository;
    private final MemberRepository memberRepository;


    @ResponseBody
    @GetMapping("/api/bestpost")
    public ResponseDto<?> best(){
        return mainService.BestList();
    }

    @ResponseBody
    @GetMapping("/api/posts")
    public ResponseDto<?> newpost(){
        return mainService.NewList();
    }

}
