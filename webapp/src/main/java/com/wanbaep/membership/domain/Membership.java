package com.wanbaep.membership.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Membership extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String membershipId;

    @Column(nullable = false)
    private String membershipName;

    @Column(nullable = false)
    private String membershipStatus;

    @Column(nullable = false)
    private int point;

    @Builder
    public Membership(String userId, String membershipId, String membershipName, String membershipStatus, int point) {
        this.userId = userId;
        this.membershipId = membershipId;
        this.membershipName = membershipName;
        this.membershipStatus = membershipStatus;
        this.point = point;
    }

    public void update(int amount) {
        this.point += amount;
    }

    public void disable() {
        this.membershipStatus = "N";
    }
}
