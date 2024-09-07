package com.team3.core.domain.sponsor.dao;

import com.team3.core.domain.member.domain.Member;
import com.team3.core.domain.sponsor.domain.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SponsorRepository extends JpaRepository<Sponsor, Long> {

    List<Sponsor> findAllByMemberOrderByCreatedAtDesc(Member member);

    @Query("SELECT s FROM Sponsor s ORDER BY s.createdAt DESC LIMIT 1")
    Optional<Sponsor> findMostRecent();
}
