package com.seven.codesnippet.Repository;

import com.seven.codesnippet.Domain.Member;
import com.seven.codesnippet.Domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMember(Member member);
}
