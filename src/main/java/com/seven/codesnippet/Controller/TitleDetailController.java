package com.seven.codesnippet.Controller;

import com.seven.codesnippet.Controller.Dto.CommentResponseDto;
import com.seven.codesnippet.Controller.Dto.PostOwnerDto;
import com.seven.codesnippet.Controller.Dto.ReCommentResponseDto;
import com.seven.codesnippet.Controller.Dto.ResponseDto;

import com.seven.codesnippet.Request.CommentPutRequestDto;
import com.seven.codesnippet.Request.CommentRequestDto;
import com.seven.codesnippet.Request.ReCommentPutRequestDto;
import com.seven.codesnippet.Request.ReCommentRequestDto;
import com.seven.codesnippet.Service.DetailService;
import com.seven.codesnippet.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TitleDetailController {


    private final DetailService detailService;

    // 게시물 좋아요
    @RequestMapping(value = "/api/posts/heart/{postId}", method = RequestMethod.POST)
    public ResponseDto<?> heartTitle(@PathVariable Long postId, HttpServletRequest request) {
        return detailService.heartTitle(postId, request);
    }



//    댓글 작성
    @RequestMapping(value = "/api/comments", method = RequestMethod.POST)
    public ResponseDto<?> createPost(@RequestBody CommentRequestDto requestDto,
                                     HttpServletRequest request) throws IOException {
        return detailService.createComment(requestDto, request);
    }

    // 게시글 상세 불러오기

    @RequestMapping(value = "/api/posts/{id}", method = RequestMethod.GET)
    public PostOwnerDto getPost(@PathVariable Long id, HttpServletRequest request) {
        return detailService.getPost(id,request);
    }

//    게시글 상세 불러오기(댓글)
    @RequestMapping(value = "/api/comments", method = RequestMethod.GET)
    public List<CommentResponseDto> getAllComments(@RequestParam Long postId, HttpServletRequest request) {
        return detailService.getAllCommentsByPost(postId,request);
    }

// 댓글 수정
    @RequestMapping(value = "/api/comments/{id}", method = RequestMethod.PUT)
    public ResponseDto<?> updateComment(@PathVariable Long id, @RequestBody CommentPutRequestDto requestDto,
                                        HttpServletRequest request) {
        return detailService.updateComment(id, requestDto, request);
    }

    // 댓글 삭제
    @RequestMapping(value = "/api/comments/{id}", method = RequestMethod.DELETE)
    public ResponseDto<?> deleteComment(@PathVariable Long id,
                                        HttpServletRequest request) {
        return detailService.deleteComment(id, request);
    }
// 대댓글 작성
    @RequestMapping(value = "/api/subcomments", method = RequestMethod.POST)
    public ResponseDto<?> createRecomment(@RequestBody ReCommentRequestDto requestDto,  // dto 안에 commentId, content
                                          HttpServletRequest request) {
        return detailService.createRecomment(requestDto, request);
    }

    // 게시글 댓글을 바라보는 **모든 대댓글 조회
    @RequestMapping(value = "/api/subcomments", method = RequestMethod.GET)
    public List<ReCommentResponseDto> getAllReComments(@RequestParam Long commentId, HttpServletRequest request) {
        return detailService.getAllReCommentsByCommentId(commentId, request);
    }

    // 대댓글 수정 기능
    @RequestMapping(value = "/api/subcomments/{id}", method = RequestMethod.PUT)
    public ResponseDto<?> updateReComment(@PathVariable Long id, @RequestBody ReCommentPutRequestDto requestDto, HttpServletRequest request) {
        return detailService.updateComment(id, requestDto, request);
    }

    // 대댓글 삭제 기능
    @RequestMapping(value = "/api/subcomments/{id}", method = RequestMethod.DELETE)
    public ResponseDto<?> deletereComment(@PathVariable Long id, HttpServletRequest request) {
        return detailService.deleteReComment(id, request);

    }
}