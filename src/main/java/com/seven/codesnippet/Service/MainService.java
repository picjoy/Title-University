package com.seven.codesnippet.Service;


import com.seven.codesnippet.Controller.Dto.ResponseDto;

import com.seven.codesnippet.Controller.Dto.ResponsePostsDto;
import com.seven.codesnippet.Controller.Dto.TitlePostListDto;
import com.seven.codesnippet.Controller.Dto.TopTenDto;
import com.seven.codesnippet.Domain.Member;
import com.seven.codesnippet.Domain.TitlePost;
import com.seven.codesnippet.Jwt.TokenProvider;
import com.seven.codesnippet.Repository.HeartRepository;
import com.seven.codesnippet.Repository.MemberRepository;
import com.seven.codesnippet.Repository.TitleCommentRepository;
import com.seven.codesnippet.Repository.TitlePostRepository;
import com.seven.codesnippet.Request.MemberRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {
    private final TitlePostRepository titlePostRepository;
    private final TitleCommentRepository titleCommentRepository;
    private final TokenProvider tokenProvider;
    private final HeartRepository heartRepository;

    public List<TopTenDto> BestList(HttpServletRequest request){
        Member member = validateMember(request);
        List<TitlePost> titlePostList = titlePostRepository.findTop10ByHeartGreaterThanOrderByCreatedAtDesc(10L);
        List<TopTenDto> toptenDto = new ArrayList<>();
        for (TitlePost titlepost :titlePostList) {
            Boolean LikeExist = heartRepository.existsByMemberAndPost(member,titlepost);
            toptenDto.add(new TopTenDto(titlepost,LikeExist));
        }

        return toptenDto;
    }

    public List<TitlePostListDto> NewList(){
        List<TitlePost> titlePostList = titlePostRepository.findTop100ByOrderByCreatedAtDesc();
        List<TitlePostListDto> titlePostListDtos = new ArrayList<>();
        for (TitlePost titlepost :titlePostList) {
            titlePostListDtos.add(new TitlePostListDto(titlepost,titleCommentRepository.countTitleCommentByPost(titlepost)));
        }
        return titlePostListDtos;
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

}
