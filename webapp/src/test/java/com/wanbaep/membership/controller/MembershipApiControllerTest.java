package com.wanbaep.membership.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanbaep.membership.domain.Membership;
import com.wanbaep.membership.domain.MembershipRepository;
import com.wanbaep.membership.dto.MembershipPointUpdateRequestDto;
import com.wanbaep.membership.dto.MembershipSaveRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MembershipApiControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
        objectMapper = new ObjectMapper();
        membershipRepository.deleteAll();
    }

    @After
    public void tearDown() {
        membershipRepository.deleteAll();
    }

    private void saveMember(String userId, String membershipId, String membershipName) {
        membershipRepository.save(Membership.builder()
                .membershipId(membershipId)
                .membershipName(membershipName)
                .point(1200)
                .userId(userId)
                .membershipStatus("Y")
                .build());
    }

    @Test
    public void getMembershipListByUserId() throws Exception {
        String userId = "test1";
        saveMember("test1", "cj", "cjone");
        saveMember("test1", "spc", "happypoint");
        saveMember("test2", "cj", "cjone");

        String url = "http://localhost:" + port + "/api/v1/membership";

        mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", userId)
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk());

        List<Membership> membershipList = membershipRepository.findByUserId(userId);
        assertThat(membershipList.size()).isEqualTo(2);
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

        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", userId)
                .header("Content-Type", "application/json")
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        List<Membership> all = membershipRepository.findAll();
        assertThat(all.get(0).getUserId()).isEqualTo(userId);
        assertThat(all.get(0).getMembershipId()).isEqualTo(membershipId);
        assertThat(all.get(0).getMembershipName()).isEqualTo(membershipName);
        assertThat(all.get(0).getPoint()).isEqualTo(point);
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

        mvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", userId)
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk());

        //check membershipStatus
        Membership entity = membershipRepository.findByUserIdAndMembershipId(userId, membershipId).orElse(null);

        assertThat(entity).isNotEqualTo(null);
        assertThat(entity.getMembershipId()).isEqualTo(membershipId);
        assertThat(entity.getUserId()).isEqualTo(userId);
        assertThat(entity.getMembershipStatus()).isEqualTo("N");
    }

    @Test
    public void getMembershipByIdAndByMembershipId() throws Exception {
        String userId = "test1";
        String membershipId = "cj";
        String membershipName = "cjone";
        saveMember(userId, membershipId, membershipName);

        String url = "http://localhost:" + port + "/api/v1/membership";

        mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", userId)
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk());

        List<Membership> all = membershipRepository.findAll();
        assertThat(all.get(0).getUserId()).isEqualTo(userId);
        assertThat(all.get(0).getMembershipId()).isEqualTo(membershipId);
        assertThat(all.get(0).getMembershipName()).isEqualTo(membershipName);
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

        mvc.perform(put(url)
                .header("X-USER-ID", userId)
                .header("Content-Type", "application/json")
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        List<Membership> all = membershipRepository.findAll();
        assertThat(all.get(0).getMembershipId()).isEqualTo(expectedMembershipId);
        assertThat(all.get(0).getPoint()).isEqualTo(expectedPoint);
    }
}
