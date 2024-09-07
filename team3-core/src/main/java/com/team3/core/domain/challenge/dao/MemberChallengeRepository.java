package com.team3.core.domain.challenge.dao;

import com.team3.core.domain.challenge.domain.MemberChallenge;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberChallengeRepository extends JpaRepository<MemberChallenge, Long> {

    @Query(
            "SELECT mc from MemberChallenge mc" +
                    " WHERE mc.member.id = :memberId" +
                    " AND mc.createdAt BETWEEN :startDate AND :endDate"
    )
    @EntityGraph(attributePaths = {"challenge.category"})
    List<MemberChallenge> findMemberChallengesByMemberIdAndDate(@Param("memberId") Long memberId,
                                                                @Param("startDate") LocalDateTime startDate,
                                                                @Param("endDate") LocalDateTime endDate);

    Optional<MemberChallenge> findByIdAndMemberId(Long challengeId, Long memberId);
}
