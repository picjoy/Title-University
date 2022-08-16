package com.seven.codesnippet.Repository;

import com.seven.codesnippet.Domain.Heart;
import com.seven.codesnippet.Domain.Member;
import com.seven.codesnippet.Domain.TitlePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {

    Long countAllByPostId(Long postId);

    Long countByMemberIdAndPostId(Long memberId, Long postId);


    @Transactional
    void deleteById(Long Id);

    List<Heart> findAllByMember(Member member);

    Boolean existsByMemberAndPost(Member member, TitlePost post);

    Heart findByMemberIdAndPostId(Long memberId, Long postId);
}
