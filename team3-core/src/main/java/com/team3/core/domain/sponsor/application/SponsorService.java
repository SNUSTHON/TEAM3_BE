package com.team3.core.domain.sponsor.application;

import com.team3.core.domain.member.dao.MemberRepository;
import com.team3.core.domain.member.domain.Member;
import com.team3.core.domain.sponsor.dao.SponsorRepository;
import com.team3.core.domain.sponsor.domain.Sponsor;
import com.team3.core.domain.sponsor.dto.MySponsorResponse;
import com.team3.core.global.exception.ErrorCode;
import com.team3.core.global.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SponsorService {

    private final SponsorRepository sponsorRepository;
    private final MemberRepository memberRepository;

    public List<MySponsorResponse> getMySponsors(Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        List<Sponsor> sponsors = sponsorRepository.findAllByMemberOrderByCreatedAtDesc(member);

        return sponsors.stream()
                .map(MySponsorResponse::new)
                .collect(Collectors.toList());
    }


}
