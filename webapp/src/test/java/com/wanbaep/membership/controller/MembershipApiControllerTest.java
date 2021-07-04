package com.wanbaep.membership.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanbaep.membership.domain.Membership;
import com.wanbaep.membership.domain.MembershipRepository;
import com.wanbaep.membership.dto.ApiResponseDto;
import com.wanbaep.membership.dto.MembershipPointUpdateRequestDto;
import com.wanbaep.membership.dto.MembershipResponseDto;
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

import java.lang.reflect.Member;
import java.util.List;
import java.util.Map;

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
        ResponseEntity<ApiResponseDto> responseEntity = restTemplate.postForEntity(url, requestEntity, ApiResponseDto.class);

        //then
        ApiResponseDto responseBody = responseEntity.getBody();
        ObjectMapper mapper = new ObjectMapper();
        MembershipResponseDto responseDto = mapper.convertValue(responseBody.getResponse(), MembershipResponseDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseBody.getSuccess()).isEqualTo(true);
        assertThat(responseDto.getUserId()).isEqualTo(userId);
        assertThat(responseDto.getMembershipId()).isEqualTo(membershipId);
        assertThat(responseDto.getMembershipName()).isEqualTo(membershipName);
        assertThat(responseDto.getPoint()).isEqualTo(point);
        assertThat(responseBody.getError()).isEqualTo(null);

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
        ResponseEntity<ApiResponseDto> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, ApiResponseDto.class);

        //then
        ApiResponseDto responseBody = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseBody.getSuccess()).isEqualTo(true);
        assertThat(responseBody.getResponse()).isEqualTo(true);
        assertThat(responseBody.getError()).isEqualTo(null);

        List<Membership> all = membershipRepository.findAll();
        assertThat(all.get(0).getMembershipId()).isEqualTo(expectedMembershipId);
        assertThat(all.get(0).getPoint()).isEqualTo(expectedPoint);
    }

    @Test
    public void disableMembership() throws Exception {
        String userId = "test1";
        String membershipId = "cj";
        Membership savedMembership = membershipRepository.save(Membership.builder()
                .membershipId(membershipId)
                .membershipName("cjone")
                .point(1200)
                .userId(userId)
                .membershipStatus("Y")
                .build());

        String url = "http://localhost:" + port + "/api/v1/membership/" + membershipId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-USER-ID", userId);
        headers.set("Content-Type", "application/json");
        HttpEntity<MembershipPointUpdateRequestDto> requestEntity = new HttpEntity<>(headers);

        //when
        ResponseEntity<ApiResponseDto> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, ApiResponseDto.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getSuccess()).isEqualTo(true);
        assertThat(responseEntity.getBody().getError()).isEqualTo(null);
        assertThat((Boolean) responseEntity.getBody().getResponse()).isEqualTo(true);

        //check membershipStatus
        Membership entity = membershipRepository.findByUserIdAndMembershipId(userId, membershipId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자의 membershipId가 없습니다. userId="+ userId + ",mebershipId=" + membershipId));

        assertThat(entity.getMembershipId()).isEqualTo(membershipId);
        assertThat(entity.getUserId()).isEqualTo(userId);
        assertThat(entity.getMembershipStatus()).isEqualTo("N");
    }
}
