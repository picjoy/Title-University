package com.seven.codesnippet.Repository;

import com.seven.codesnippet.Domain.TitleComment;
import com.seven.codesnippet.Domain.TitlePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TitlePostRepository extends JpaRepository<TitlePost, Long> {
    List<TitlePost> findByHeartGreaterThanOrderByCreatedAtDesc(Long than);
}
