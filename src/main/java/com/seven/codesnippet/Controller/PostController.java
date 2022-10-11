package com.seven.codesnippet.Controller;

import com.seven.codesnippet.Controller.Dto.ResponseDto;
import com.seven.codesnippet.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    // 게시글 작성
    @RequestMapping(value = "/api/posts", method = RequestMethod.POST)
    public ResponseDto<?> createPost(@RequestPart(value = "file") MultipartFile multipartFile,
                                     @RequestPart String title,
                                     HttpServletRequest request) throws IOException {
        return postService.createPost(title, request,multipartFile);
    }


    // 게시글 삭제
    @RequestMapping(value = "/api/posts/{id}", method = RequestMethod.DELETE)
    public ResponseDto<?> deletePost(@PathVariable Long id,
                                     HttpServletRequest request) {
        return postService.deletePost(id, request);
    }

}
