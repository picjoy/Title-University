package com.seven.codesnippet.Service;


import com.seven.codesnippet.Controller.Dto.ResponseDto;

import com.seven.codesnippet.Controller.Dto.TitlePostListDto;
import com.seven.codesnippet.Controller.Dto.TitlePostListListDto;
import com.seven.codesnippet.Domain.TitlePost;
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

    public ResponseDto<?> BestList(){
        List<TitlePost> titlePostList = titlePostRepository.findByHeartGreaterThanOrderByCreatedAtDesc(10L);
        List<TitlePostListDto> titlePostListDtos = new ArrayList<>();
        for (TitlePost titlepost :titlePostList) {
            titlePostListDtos.add(new TitlePostListDto(titlepost));
        }

        return ResponseDto.success(new TitlePostListListDto(titlePostListDtos));
    }


}
