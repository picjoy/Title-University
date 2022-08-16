package com.seven.codesnippet.Repository;

import com.seven.codesnippet.Domain.Heart;
import com.seven.codesnippet.Domain.Member;
import com.seven.codesnippet.Domain.TitlePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {


    List<Heart> findAllByMember(String member);

    Boolean existsByMemberAndPost(String member, TitlePost post);
}
