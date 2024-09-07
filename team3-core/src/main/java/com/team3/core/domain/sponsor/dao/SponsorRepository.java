package com.team3.core.domain.sponsor.dao;

import com.team3.core.domain.member.domain.Member;
import com.team3.core.domain.sponsor.domain.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SponsorRepository extends JpaRepository<Sponsor, Long> {

    List<Sponsor> findAllByMemberOrderByCreatedAtDesc(Member member);
}
