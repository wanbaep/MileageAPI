package com.wanbaep.membership.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanbaep.membership.domain.MembershipRepository;
import com.wanbaep.membership.dto.ApiResponseDto;
import com.wanbaep.membership.dto.ErrorResponse;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommonExceptionHandlerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @After
    public void cleanup() {
        membershipRepository.deleteAll();
    }

    private ApiResponseDto createExpectResponse(int status, String message) {
        ApiResponseDto response = new ApiResponseDto();
        response.setSuccess(false);
        response.setResponse(null);
        response.setError(new ErrorResponse(status, message));
        return response;
    }

    @Test
    public void handleBadRequestException() throws Exception {
        String url = "http://localhost:" + port + "/api/v1/membership";
        ApiResponseDto response = createExpectResponse(400, "Bad Request");

        mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(response)));
    }

    @Test
    public void handleUserNotFoundException() throws Exception {
        String userId = "test1";
        String membershipId = "cj";
        String url = "http://localhost:" + port + "/api/v1/membership/" + membershipId;
        String message = "Can not find userId("+userId+"), membershipId("+membershipId+")";
        ApiResponseDto response = createExpectResponse(404, message);

        mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", userId)
                .header("Content-Type", "application/json"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(response)));
    }

    @Test
    public void handleDuplicateMembershipException() throws Exception {
        String userId = "test1";
        String membershipId = "cj";
        String membershipName = "cjone";
        int point = 1200;

        MembershipSaveRequestDto requestDto = MembershipSaveRequestDto.builder().membershipId(membershipId).membershipName(membershipName).point(point).build();
        membershipRepository.save(requestDto.toEntity(userId));
        String message = "Duplicated membership, userId(" + userId + "), membershipId("+ membershipId+")";
        ApiResponseDto response = createExpectResponse(400, message);

        String url = "http://localhost:" + port + "/api/v1/membership";
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-USER-ID", userId)
                .header("Content-Type", "application/json")
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(response)));
    }
}