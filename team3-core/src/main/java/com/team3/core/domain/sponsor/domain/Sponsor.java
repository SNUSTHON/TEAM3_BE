package com.team3.core.domain.sponsor.domain;

import com.team3.core.domain.member.domain.Member;
import com.team3.core.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sponsor extends BaseEntity {

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private String institution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false)
    private Member member;

    @Builder
    public Sponsor(Long amount, String institution, Member member) {
        this.amount = amount;
        this.institution = institution;
        this.member = member;
    }
}
