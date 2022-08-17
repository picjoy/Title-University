package com.seven.codesnippet.Repository;

import com.seven.codesnippet.Domain.TitleComment;
import com.seven.codesnippet.Domain.TitlePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Repository
public interface TitleCommentRepository extends JpaRepository<TitleComment, Long> {
    List<TitleComment> findByPostOrderByCreatedAtDesc(TitlePost post);
    Long countTitleCommentByPost(TitlePost post);

}
