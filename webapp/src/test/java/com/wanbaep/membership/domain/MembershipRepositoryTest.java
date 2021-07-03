package com.wanbaep.membership.domain;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MembershipRepositoryTest {

    @Autowired
    MembershipRepository membershipRepository;

    @After
    public void cleanup() {
        membershipRepository.deleteAll();
    }

    @Test
    public void loadMembership() {
        String userId = "test1";
        String membershipId = "spc";
        String membershipName = "happypoint";
        String membershipStatus = "Y";
        int point = 120;

        membershipRepository.save(Membership.builder()
                .userId(userId)
                .membershipId(membershipId)
                .membershipName(membershipName)
                .membershipStatus(membershipStatus)
                .point(point)
                .build());

        List<Membership> membershipList = membershipRepository.findAll();

        Membership membership = membershipList.get(0);
        assertThat(membership.getUserId()).isEqualTo(userId);
        assertThat(membership.getMembershipId()).isEqualTo(membershipId);
        assertThat(membership.getMembershipName()).isEqualTo(membershipName);
        assertThat(membership.getMembershipStatus()).isEqualTo(membershipStatus);
        assertThat(membership.getPoint()).isEqualTo(point);
    }
}
