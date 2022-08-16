package com.seven.codesnippet.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.seven.codesnippet.Controller.Dto.PostOwnerDto;
import com.seven.codesnippet.Controller.Dto.ResponseDto;
import com.seven.codesnippet.Controller.Dto.TopTenDto;
import com.seven.codesnippet.Domain.Member;
import com.seven.codesnippet.Domain.TitlePost;
import com.seven.codesnippet.Jwt.TokenProvider;
import com.seven.codesnippet.Repository.HeartRepository;
import com.seven.codesnippet.Repository.TitleCommentRepository;
import com.seven.codesnippet.Repository.TitlePostRepository;
import com.seven.codesnippet.Repository.TitleSubCommentRepository;
import com.seven.codesnippet.Shared.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {


    private final TitlePostRepository titlePostRepository;
    private final TokenProvider tokenProvider;
    private final HeartRepository heartRepository;
    private final AmazonS3Client amazonS3Client;


    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    // 게시글 작성
    @Transactional
    public ResponseDto<?> createPost(String title, HttpServletRequest request, MultipartFile multipartFile) throws IOException {
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
        String imgurl = null;

        if (!multipartFile.isEmpty()) {
            String fileName = CommonUtils.buildFileName(multipartFile.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());

            byte[] bytes = IOUtils.toByteArray(multipartFile.getInputStream());
            objectMetadata.setContentLength(bytes.length);
            ByteArrayInputStream byteArrayIs = new ByteArrayInputStream(bytes);

            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, byteArrayIs, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            imgurl = amazonS3Client.getUrl(bucketName, fileName).toString();
        }


        TitlePost post = TitlePost.builder()
                .title(title)
                .member(member)
                .image(imgurl)
                .build();
        titlePostRepository.save(post);
        return ResponseDto.success("작성이 완료되었습니다.");
    }



    @Transactional
    public ResponseDto<?> deletePost(Long id, HttpServletRequest request) {
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

        TitlePost post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        if (post.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
        }

        titlePostRepository.delete(post);
        return ResponseDto.success("삭제가 완료되었습니다.");
    }

    @Transactional(readOnly = true)
    public TitlePost isPresentPost(Long id) {
        Optional<TitlePost> optionalPost = titlePostRepository.findById(id);
        return optionalPost.orElse(null);
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

}
