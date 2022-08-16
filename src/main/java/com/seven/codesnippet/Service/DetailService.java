package com.seven.codesnippet.Service;


import com.seven.codesnippet.Controller.Dto.CommentResponseDto;
import com.seven.codesnippet.Controller.Dto.PostOwnerDto;
import com.seven.codesnippet.Controller.Dto.ReCommentResponseDto;
import com.seven.codesnippet.Controller.Dto.ResponseDto;
import com.seven.codesnippet.Domain.Member;
import com.seven.codesnippet.Domain.TitleComment;
import com.seven.codesnippet.Domain.TitlePost;
import com.seven.codesnippet.Domain.TitleSubComment;
import com.seven.codesnippet.Jwt.TokenProvider;
import com.seven.codesnippet.Repository.HeartRepository;
import com.seven.codesnippet.Repository.TitleCommentRepository;
import com.seven.codesnippet.Repository.TitlePostRepository;
import com.seven.codesnippet.Repository.TitleSubCommentRepository;
import com.seven.codesnippet.Request.CommentPutRequestDto;
import com.seven.codesnippet.Request.CommentRequestDto;
import com.seven.codesnippet.Request.ReCommentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DetailService {

    private final TitleCommentRepository titleCommentRepository;
    private final TitleSubCommentRepository titleSubCommentRepository;
    private final TitlePostRepository postRepository;
    private final TokenProvider tokenProvider;
    private final PostService postService;
    private final HeartRepository heartRepository;


    @Transactional
    public ResponseDto<?> createComment(CommentRequestDto requestDto, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        TitlePost post = postService.isPresentPost(requestDto.getPostId());
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        TitleComment comment = TitleComment.builder()
                .member(member.getNickname())
                .post(post)
                .content(requestDto.getContent())
                .build();
        titleCommentRepository.save(comment);
        return ResponseDto.success("작성에 성공했습니다."
        );
    }

    // 특정 게시글 조회
    @Transactional(readOnly = true)
    public ResponseDto<?> getPost(Long id, HttpServletRequest request) {  // 게시글의 id
        TitlePost post = postService.isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }
        Member member = validateMember(request);
        boolean LikeExist;
        boolean postowner;
//    로그인 되어있을시
        if (member != null) {
            LikeExist = heartRepository.existsByMemberAndPost(member.getNickname(), post);
            postowner = Objects.equals(post.getMember().getNickname(), member.getNickname());
        }
//    로그인 안되어있을시
        else {
            LikeExist = false;
            postowner = false;
        }
        PostOwnerDto posts = new PostOwnerDto(post,LikeExist,postowner);
        return ResponseDto.success(posts);
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllCommentsByPost(Long postId, HttpServletRequest request) {
        TitlePost post = postService.isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }
        Member member = validateMember(request);
        List<TitleComment> commentList = titleCommentRepository.findAllByPost(post);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        boolean commentowner;
//    로그인 되어있을시
        if (member != null) {
            commentowner = Objects.equals(post.getMember().getNickname(), member.getNickname());
        }
//    로그인 안되어있을시
        else {
            commentowner = false;
        }

        for (TitleComment comment : commentList) {
            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .id(comment.getId())
                            .userCommented(comment.getMember())
                            .comment(comment.getContent())
                            .postId(comment.getPost().getId())
                            .commentOwner(commentowner)
                            .subcomment_num((long) commentList.size())
                            .build()
            );
        }
        return ResponseDto.success(commentResponseDtoList);
    }

    @Transactional
    public ResponseDto<?> updateComment(Long id, CommentPutRequestDto requestDto, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        TitlePost post = postService.isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        TitleComment comment = isPresentComment(id);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        if (comment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        comment.update(requestDto);
        return ResponseDto.success("수정에 성공했습니다."
        );
    }

    @Transactional
    public ResponseDto<?> deleteComment(Long id, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        TitleComment comment = isPresentComment(id);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        if (comment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        titleCommentRepository.delete(comment);
        return ResponseDto.success("success");
    }

    @Transactional(readOnly = true)
    public TitleComment isPresentComment(Long id) {
        Optional<TitleComment> optionalComment = titleCommentRepository.findById(id);
        return optionalComment.orElse(null);
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }


    //--------------------------------------------------------------------------------------------------
    @Transactional  // 공통으로 들어가는 Response에 대한 Dto
    public ResponseDto<?> createRecomment(ReCommentRequestDto requestDto, HttpServletRequest request) {
        // 요청의 헤더에 토큰 있늬?
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        // 헤더에 Auth 토큰 있늬?
        // Refresh와 Authorization 한 번에 할 수 없나?
        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        // 위에서 토큰 확인 후, 이용자 검증 로직
        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        // 대댓글이 바라보고 있는 댓글 존재 검증
        TitleComment comment = isPresentComment(requestDto.getCommentId());
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        TitleSubComment reComment = TitleSubComment.builder()
                .member(member.getNickname())
                .comment(comment)
                .content(requestDto.getContents())
                .build();
        titleSubCommentRepository.save(reComment);
        return ResponseDto.success("작성 완료되었습니다."
        );
    }

    // 특정 댓글을 바라보는 모든 대댓글 조회
    @Transactional(readOnly = true)
    public ResponseDto<?> getAllReCommentsByCommentId(Long commentId) {
        TitleComment comment = isPresentComment(commentId);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        List<TitleSubComment> reCommentList = titleSubCommentRepository.findAllByCommentId(commentId);
        List<ReCommentResponseDto> reCommentResponseDtoList = new ArrayList<>();

        for (TitleSubComment reComment : reCommentList) {
            reCommentResponseDtoList.add(new ReCommentResponseDto(reComment)
            );
        }
        return ResponseDto.success(reCommentResponseDtoList);
    }

    @Transactional
    public ResponseDto<?> updateComment(Long id, ReCommentRequestDto requestDto, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        TitleComment comment = isPresentComment(requestDto.getCommentId());
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        TitleSubComment reComment = isPresentReComment(id);
        if (null == reComment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        if (reComment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        reComment.update(requestDto);
        return ResponseDto.success("수정에 성공했습니다."
        );
    }

    @Transactional
    public ResponseDto<?> deleteReComment(Long id, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        TitleSubComment reComment = isPresentReComment(id);
        if (null == reComment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        if (reComment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        titleSubCommentRepository.delete(reComment);
        return ResponseDto.success("success");
    }

    @Transactional(readOnly = true)
    public TitleSubComment isPresentReComment(Long id) {
        Optional<TitleSubComment> optionalReComment = titleSubCommentRepository.findById(id);
        return optionalReComment.orElse(null);
    }


}
