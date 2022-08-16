package com.seven.codesnippet.Service;


import com.seven.codesnippet.Controller.Dto.ResponseDto;

import com.seven.codesnippet.Controller.Dto.TitlePostListDto;
import com.seven.codesnippet.Controller.Dto.TopTenDto;
import com.seven.codesnippet.Domain.Member;
import com.seven.codesnippet.Domain.TitlePost;
import com.seven.codesnippet.Repository.HeartRepository;
import com.seven.codesnippet.Repository.MemberRepository;
import com.seven.codesnippet.Repository.TitleCommentRepository;
import com.seven.codesnippet.Repository.TitlePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {
    private final TitlePostRepository titlePostRepository;
    private final TitleCommentRepository titleCommentRepository;
    private final MemberRepository memberRepository;
    private final HeartRepository heartRepository;

    public ResponseDto<?> BestList(){
        Member member = memberRepository.getReferenceById(1L);
        List<TitlePost> titlePostList = titlePostRepository.findTop10ByHeartGreaterThanOrderByHeartDesc(10L);
        List<TopTenDto> toptenDto = new ArrayList<>();
        for (TitlePost titlepost :titlePostList) {
            Boolean LikeExist = heartRepository.existsByMemberAndPost(member,titlepost);
            toptenDto.add(new TopTenDto(titlepost,LikeExist));
        }

        return ResponseDto.success(toptenDto);
    }

    public ResponseDto<?> NewList(){
        List<TitlePost> titlePostList = titlePostRepository.findTop100ByOrderByCreatedAtDesc();
        List<TitlePostListDto> titlePostListDtos = new ArrayList<>();
        for (TitlePost titlepost :titlePostList) {
            titlePostListDtos.add(new TitlePostListDto(titlepost));
        }
        return ResponseDto.success(titlePostListDtos);
    }


}
