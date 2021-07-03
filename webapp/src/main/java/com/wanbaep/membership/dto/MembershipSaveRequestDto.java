package com.wanbaep.membership.dto;

import com.wanbaep.membership.domain.Membership;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MembershipSaveRequestDto {
    private String membershipId;
    private String membershipName;
    private String point;

    @Builder
    public MembershipSaveRequestDto(String membershipId, String membershipName, String point) {
        this.membershipId = membershipId;
        this.membershipName = membershipName;
        this.point = point;
    }

    public Membership toEntity() {
        return Membership.builder()
                .membershipId(membershipId)
                .membershipName(membershipName)
                .point(point)
                .userId("test1")
                .membershipStatus("Y")
                .build();
    }
}
