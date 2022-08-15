package com.seven.codesnippet.Repository;

import com.seven.codesnippet.Domain.Heart;
import com.seven.codesnippet.Domain.Member;
import com.seven.codesnippet.Domain.TitlePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {
    Boolean existsByAutherAndPost(String auther, TitlePost post);
}
