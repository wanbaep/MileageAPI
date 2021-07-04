package com.wanbaep.membership.service;

import com.wanbaep.membership.domain.Membership;
import com.wanbaep.membership.domain.MembershipRepository;
import com.wanbaep.membership.dto.MembershipPointUpdateRequestDto;
import com.wanbaep.membership.dto.MembershipResponseDto;
import com.wanbaep.membership.dto.MembershipSaveRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MembershipServiceTest {
    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private MembershipService membershipService;

    @Before
    public void setup() {
        membershipRepository.deleteAll();
    }

    @After
    public void tearDown() {
        membershipRepository.deleteAll();
    }

    private Membership saveMember(String userId, String membershipId, String membershipName) {
        Membership membership = membershipRepository.save(Membership.builder()
                .membershipId(membershipId)
                .membershipName(membershipName)
                .point(1200)
                .userId(userId)
                .membershipStatus("Y")
                .build());
        return membership;
    }

    @Test
    public void findByUserId() throws Exception {
        saveMember("test1", "cj", "cjone");
        saveMember("test2", "cj", "cjone");
        saveMember("test2", "kt", "ktmembership");

        List<MembershipResponseDto> entityList = membershipService.findByUserId("test1");
        assertThat(entityList.size()).isEqualTo(1);
        assertThat(entityList.get(0).getUserId()).isEqualTo("test1");
        assertThat(entityList.get(0).getMembershipId()).isEqualTo("cj");
        assertThat(entityList.get(0).getMembershipName()).isEqualTo("cjone");
    }

    @Test
    public void save() {
        MembershipResponseDto expected = membershipService.save("test1", MembershipSaveRequestDto.builder()
                .membershipId("spc")
                .membershipName("happypoint")
                .point(5100).build());

        List<Membership> membershipList = membershipRepository.findAll();
        assertThat(membershipList.get(0).getUserId()).isEqualTo(expected.getUserId());
        assertThat(membershipList.get(0).getMembershipId()).isEqualTo(expected.getMembershipId());
        assertThat(membershipList.get(0).getMembershipName()).isEqualTo(expected.getMembershipName());
        assertThat(membershipList.get(0).getPoint()).isEqualTo(expected.getPoint());
        assertThat(membershipList.get(0).getMembershipStatus()).isEqualTo(expected.getMembershipStatus());
    }

    @Test
    public void disable() {
        Membership expected = saveMember("test1", "spc", "happypoint");
        membershipService.disable("test1", "spc");

        List<Membership> membershipList = membershipRepository.findAll();
        assertThat(membershipList.get(0).getUserId()).isEqualTo(expected.getUserId());
        assertThat(membershipList.get(0).getMembershipId()).isEqualTo(expected.getMembershipId());
        assertThat(membershipList.get(0).getMembershipName()).isEqualTo(expected.getMembershipName());
        assertThat(membershipList.get(0).getPoint()).isEqualTo(expected.getPoint());
        assertThat(membershipList.get(0).getMembershipStatus()).isEqualTo("N");
    }

    @Test
    public void findByUserIdAndMembershipId() {
        saveMember("test1", "cj", "cjone");
        saveMember("test2", "cj", "cjone");
        saveMember("test1", "kt", "ktmembership");

        MembershipResponseDto membership = membershipService.findByUserIdAndMembershipId("test1", "cj");
        assertThat(membership.getUserId()).isEqualTo("test1");
        assertThat(membership.getMembershipId()).isEqualTo("cj");
        assertThat(membership.getMembershipName()).isEqualTo("cjone");
    }

    @Test
    public void update() {
        Membership savedMembership = saveMember("test1", "kt", "ktmembership");
        int updatePoint = 300;
        MembershipPointUpdateRequestDto requestDto = MembershipPointUpdateRequestDto.builder()
                .membershipId("kt")
                .amount(updatePoint)
                .build();
        int expectedPoint = savedMembership.getPoint() + updatePoint;

        MembershipResponseDto expected = membershipService.update("test1", requestDto);

        List<Membership> membershipList = membershipRepository.findAll();
        assertThat(membershipList.get(0).getUserId()).isEqualTo(expected.getUserId());
        assertThat(membershipList.get(0).getMembershipId()).isEqualTo(expected.getMembershipId());
        assertThat(membershipList.get(0).getMembershipName()).isEqualTo(expected.getMembershipName());
        assertThat(membershipList.get(0).getPoint()).isEqualTo(expected.getPoint());
        assertThat(membershipList.get(0).getPoint()).isEqualTo(expectedPoint);
    }
}