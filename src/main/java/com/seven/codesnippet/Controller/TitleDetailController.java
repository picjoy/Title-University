package com.seven.codesnippet.Controller;

import com.seven.codesnippet.Controller.Dto.ResponseDto;
import com.seven.codesnippet.Service.DetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class TitleDetailController {

    private final DetailService detailService;

    // 게시물 좋아요
    @ResponseBody
    @PostMapping("/api/posts/heart/{postId}")
    public ResponseDto<?> heartTitle(@PathVariable Long postId, HttpServletRequest request) {
        return detailService.heartTitle(postId, request);
    }
}