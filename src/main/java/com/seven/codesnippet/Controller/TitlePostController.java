package com.seven.codesnippet.Controller;

import com.seven.codesnippet.Controller.Dto.ResponseDto;
import com.seven.codesnippet.Controller.Dto.ResponsePostsDto;
import com.seven.codesnippet.Controller.Dto.TitlePostListDto;
import com.seven.codesnippet.Controller.Dto.TopTenDto;
import com.seven.codesnippet.Domain.Member;
import com.seven.codesnippet.Domain.TitlePost;
import com.seven.codesnippet.Repository.MemberRepository;
import com.seven.codesnippet.Repository.TitlePostRepository;
import com.seven.codesnippet.Request.MemberRequestDto;
import com.seven.codesnippet.Service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TitlePostController {

    private final MainService mainService;

    @ResponseBody
    @GetMapping("/api/bestpost")
    public List<TopTenDto> topten(HttpServletRequest request){
        return mainService.BestList(request);
    }

    @ResponseBody
    @GetMapping("/api/posts")
    public List<TitlePostListDto> posts(){
        return mainService.NewList();
    }

}
