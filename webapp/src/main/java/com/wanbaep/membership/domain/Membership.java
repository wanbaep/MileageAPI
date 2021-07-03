package com.wanbaep.membership.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String membershipId;

    @Column(nullable = false)
    private String membershipName;

//    @Column(nullable = true)
//    private Date startDate;

    @Column(nullable = false)
    private String membershipStatus;

    @Column(nullable = false)
    private String point;

    @Builder
    public Membership(String userId, String membershipId, String membershipName, String membershipStatus, String point) {
        this.userId = userId;
        this.membershipId = membershipId;
        this.membershipName = membershipName;
        this.membershipStatus = membershipStatus;
        this.point = point;
    }
}
