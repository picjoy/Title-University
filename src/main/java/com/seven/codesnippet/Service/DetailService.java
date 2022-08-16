package com.seven.codesnippet.Service;

import com.seven.codesnippet.Controller.Dto.DetailResponseDto;
import com.seven.codesnippet.Controller.Dto.ResponseDto;
import com.seven.codesnippet.Domain.Heart;
import com.seven.codesnippet.Domain.Member;
import com.seven.codesnippet.Domain.TitlePost;
import com.seven.codesnippet.Jwt.TokenProvider;
import com.seven.codesnippet.Repository.HeartRepository;
import com.seven.codesnippet.Repository.TitlePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class DetailService {

    private final PostService postService;
    private final TitlePostRepository titlePostRepository;
    private final HeartRepository heartRepository;
    private final TokenProvider tokenProvider;


    // 게시글 좋아요 & 취소
    public ResponseDto<?> heartTitle(Long postId, HttpServletRequest request) {

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
        // 게시글 존재 확인
        TitlePost post = postService.isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        // 게시글 좋아요 생성 & 삭제
        Long checkHeart = heartRepository.countByMemberIdAndPostId(member.getId(),post.getId());
        if(checkHeart > 0){
            // 게시글 좋아요 수 삭제
                heartRepository.deleteById(heartRepository.findByMemberIdAndPostId(member.getId(),post.getId()).getId());
        } else {
            Heart heart = Heart.builder()
                    .member(member)
                    .post(post)
                    .build();
            heartRepository.save(heart);
        }

        Long heart = heartRepository.countAllByPostId(post.getId());
        post.updateHeart(heart);
        titlePostRepository.save(post);

        DetailResponseDto detailResponseDto = DetailResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .userNickname(post.getMember().getNickname())
                .imageUrl(post.getImage())
                .like_num(post.getHeart())
                .build();

        return ResponseDto.success(detailResponseDto);




    }

    private Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }





}
