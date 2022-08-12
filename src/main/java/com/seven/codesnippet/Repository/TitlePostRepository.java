package com.seven.codesnippet.Repository;

import com.seven.codesnippet.Domain.TitlePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TitlePostRepository extends JpaRepository<TitlePost, Long> {
}
