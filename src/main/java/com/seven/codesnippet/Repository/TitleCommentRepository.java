package com.seven.codesnippet.Repository;

import com.seven.codesnippet.Domain.TitleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TitleCommentRepository extends JpaRepository<TitleComment, Long> {
}
