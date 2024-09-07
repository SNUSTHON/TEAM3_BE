package com.team3.core.domain.sponsor.dto;

import com.team3.core.domain.sponsor.domain.Sponsor;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MySponsorResponse {

    private Long sponsorId;
    private Long amount;
    private String institution;

    public MySponsorResponse(Sponsor sponsor) {
        this.sponsorId = sponsor.getId();
        this.amount = sponsor.getAmount();
        this.institution = sponsor.getInstitution();
    }
}
