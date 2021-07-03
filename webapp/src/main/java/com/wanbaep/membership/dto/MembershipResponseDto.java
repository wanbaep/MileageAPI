package com.wanbaep.membership.dto;

import com.wanbaep.membership.domain.Membership;
import lombok.Getter;

@Getter
public class MembershipResponseDto {
    private Long seq;
    private String membershipId;
    private String membershipName;
    private String userId;
    private int point;
    private String membershipStatus;

    public MembershipResponseDto(Membership entity) {
        this.seq = entity.getSeq();
        this.membershipId = entity.getMembershipId();
        this.membershipName = entity.getMembershipName();
        this.userId = entity.getUserId();
        this.point = entity.getPoint();
        this.membershipStatus = entity.getMembershipStatus();
    }
}
