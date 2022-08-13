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

@Controller
@RequiredArgsConstructor
public class TitlePostController {

    private final MainService mainService;
    private final TitlePostRepository titlePostRepository;
    private final MemberRepository memberRepository;

    @ResponseBody
    @PostMapping("/api/posts/")
    public String post(){
        Member member = memberRepository.getReferenceById(1L);
        for (int i = 0; i <20 ; i++) {
            TitlePost titlePost = new TitlePost("nickname",member, (long) i,"image");
            titlePostRepository.save(titlePost);
        }

        return "게시글 작성완료!";
    }


    @ResponseBody
    @GetMapping("/api/posts/detail/comments")
    public ResponseDto<?> best(){
        return mainService.BestList();
    }




}
