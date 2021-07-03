package com.wanbaep.membership.controller;

import com.wanbaep.membership.domain.Membership;
import com.wanbaep.membership.domain.MembershipRepository;
import com.wanbaep.membership.dto.MembershipSaveRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        String point = "1200";
        MembershipSaveRequestDto requestDto = MembershipSaveRequestDto.builder()
                .membershipId(membershipId)
                .membershipName(membershipName)
                .point(point).build();

        String url = "http://localhost:" + port + "/api/v1/membership";

        //when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Membership> all = membershipRepository.findAll();
        assertThat(all.get(0).getMembershipId()).isEqualTo(membershipId);
        assertThat(all.get(0).getMembershipName()).isEqualTo(membershipName);
        assertThat(all.get(0).getPoint()).isEqualTo(point);

    }
}
