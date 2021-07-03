package com.wanbaep.membership.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MembershipPointUpdateRequestDto {
    private String membershipId;
    private int amount;

    @Builder
    public MembershipPointUpdateRequestDto(String membershipId, int amount) {
        this.membershipId = membershipId;
        this.amount = amount;
    }
}
