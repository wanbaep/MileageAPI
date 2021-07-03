package com.wanbaep.membership.controller;

import com.wanbaep.membership.domain.Membership;
import com.wanbaep.membership.domain.MembershipRepository;
import com.wanbaep.membership.dto.MembershipPointUpdateRequestDto;
import com.wanbaep.membership.dto.MembershipSaveRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MembershipApiControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MembershipRepository membershipRepository;

    @After
    public void tearDown() throws Exception {
        membershipRepository.deleteAll();
    }

    @Test
    public void registerMembership() throws Exception {
        String membershipId = "cj";
        String membershipName = "cjone";
        int point = 1200;
        MembershipSaveRequestDto requestDto = MembershipSaveRequestDto.builder()
                .membershipId(membershipId)
                .membershipName(membershipName)
                .point(point).build();

        String url = "http://localhost:" + port + "/api/v1/membership";
        String userId = "test1";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-USER-ID", userId);
        headers.set("Content-Type", "application/json");
        HttpEntity<MembershipSaveRequestDto> requestEntity = new HttpEntity<>(requestDto, headers);

        //when
        ResponseEntity<Membership> responseEntity = restTemplate.postForEntity(url, requestEntity, Membership.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getUserId()).isEqualTo(userId);
        assertThat(responseEntity.getBody().getMembershipId()).isEqualTo(membershipId);
        assertThat(responseEntity.getBody().getMembershipName()).isEqualTo(membershipName);
        assertThat(responseEntity.getBody().getPoint()).isEqualTo(point);

        List<Membership> all = membershipRepository.findAll();
        assertThat(all.get(0).getMembershipId()).isEqualTo(membershipId);
        assertThat(all.get(0).getMembershipName()).isEqualTo(membershipName);
        assertThat(all.get(0).getPoint()).isEqualTo(point);
    }

    @Test
    public void accumulatePoint() throws Exception {
        String userId = "test1";
        Membership savedMembership = membershipRepository.save(Membership.builder()
                .membershipId("cj")
                .membershipName("cjone")
                .point(1200)
                .userId(userId)
                .membershipStatus("Y")
                .build());

        String expectedMembershipId = savedMembership.getMembershipId();
        int updatePoint = 250;
        int expectedPoint = savedMembership.getPoint() + updatePoint;

        MembershipPointUpdateRequestDto requestDto = MembershipPointUpdateRequestDto.builder()
                .membershipId(expectedMembershipId)
                .amount(updatePoint)
                .build();

        String url = "http://localhost:" + port + "/api/v1/membership/point";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-USER-ID", userId);
        headers.set("Content-Type", "application/json");
        HttpEntity<MembershipPointUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto, headers);

        //when
        ResponseEntity<Membership> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Membership.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getMembershipId()).isEqualTo(expectedMembershipId);
        assertThat(responseEntity.getBody().getUserId()).isEqualTo(userId);

        List<Membership> all = membershipRepository.findAll();
        assertThat(all.get(0).getMembershipId()).isEqualTo(expectedMembershipId);
        assertThat(all.get(0).getPoint()).isEqualTo(expectedPoint);
    }
}
