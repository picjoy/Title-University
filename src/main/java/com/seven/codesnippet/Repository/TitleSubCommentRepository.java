package com.seven.codesnippet.Repository;

import com.seven.codesnippet.Domain.TitleSubComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TitleSubCommentRepository extends JpaRepository<TitleSubComment, Long> {
    List<TitleSubComment> findAllByCommentIdOrderByCreatedAtDesc(Long id);
}
