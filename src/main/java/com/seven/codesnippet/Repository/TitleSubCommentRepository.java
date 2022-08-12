package com.seven.codesnippet.Repository;

import com.seven.codesnippet.Domain.TitleSubComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TitleSubCommentRepository extends JpaRepository<TitleSubComment, Long> {
}
