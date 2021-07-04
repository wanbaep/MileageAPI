package com.wanbaep.membership.dto;

import com.wanbaep.membership.domain.Membership;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class MembershipResponseDto {
    private Long seq;
    private String membershipId;
    private String userId;
    private String membershipName;
    private LocalDateTime startDate;
    private String membershipStatus;
    private int point;

    public MembershipResponseDto(Membership entity) {
        this.seq = entity.getSeq();
        this.membershipId = entity.getMembershipId();
        this.userId = entity.getUserId();
        this.membershipName = entity.getMembershipName();
        this.startDate = entity.getStartDate();
        this.membershipStatus = entity.getMembershipStatus();
        this.point = entity.getPoint();
    }
}
