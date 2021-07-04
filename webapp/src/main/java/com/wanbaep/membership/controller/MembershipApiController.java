package com.wanbaep.membership.controller;

import com.wanbaep.membership.dto.ApiResponseDto;
import com.wanbaep.membership.dto.MembershipPointUpdateRequestDto;
import com.wanbaep.membership.dto.MembershipResponseDto;
import com.wanbaep.membership.dto.MembershipSaveRequestDto;
import com.wanbaep.membership.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1")
public class MembershipApiController {
    private final MembershipService membershipService;
    Logger logger = LoggerFactory.getLogger(MembershipApiController.class);

    @GetMapping("membership")
    public ResponseEntity<?> getMembershipListByUserId(
            @RequestHeader(value="X-USER-ID") String userId) {
        logger.debug("Get MembershipList userId({})", userId);
        List<MembershipResponseDto> responseDtoList = membershipService.findByUserId(userId);
        ApiResponseDto apiResponseDto = new ApiResponseDto(true, responseDtoList, null);
        logger.info("GET api/v1/membership HTTP STATUS OK");
        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }

    @PostMapping("membership")  //@RequestBody
    public ResponseEntity<?> registerMembership(
            @RequestHeader(value="X-USER-ID") String userId,
            @RequestBody MembershipSaveRequestDto requestDto) {
        logger.debug("Register Membership userId({}), membershipId({})", userId, requestDto.getMembershipId());
        MembershipResponseDto responseDto = membershipService.save(userId, requestDto);
        ApiResponseDto apiResponseDto = new ApiResponseDto(true, responseDto, null);
        logger.info("POST api/v1/membership HTTP STATUS OK");
        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("membership/{membershipId}")
    public ResponseEntity<?> disableMembership(
            @RequestHeader(value="X-USER-ID") String userId,
            @PathVariable("membershipId") String membershipId) {
        logger.debug("Disable Membership userId({}), membershipId({})", userId, membershipId);
        ApiResponseDto apiResponseDto = new ApiResponseDto(true, true, null);
        membershipService.disable(userId, membershipId);
        logger.info("DELETE api/v1/membership/{membershipId} HTTP STATUS OK");
        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }

    @GetMapping("membership/{membershipId}")
    public ResponseEntity<?> getMembershipByIdAndByMembershipId(
            @RequestHeader(value="X-USER-ID") String userId,
            @PathVariable("membershipId") String membershipId) {
        logger.debug("get Membership userId({}), membershipId({})", userId, membershipId);
        MembershipResponseDto responseDto = membershipService.findByUserIdAndMembershipId(userId, membershipId);
        ApiResponseDto apiResponseDto = new ApiResponseDto(true, responseDto, null);
        logger.info("GET api/v1/membership/{membershipId} HTTP STATUS OK");
        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }

    @PutMapping("membership/point") //@RequestBody
    public ResponseEntity<?> accumulatePoint(
            @RequestHeader(value="X-USER-ID") String userId,
            @RequestBody MembershipPointUpdateRequestDto requestDto) {
        logger.debug("Accumulate Membership Point userId({}), membershipId({})", userId, requestDto.getMembershipId());
        MembershipResponseDto responseDto = membershipService.update(userId, requestDto);
        ApiResponseDto apiResponseDto = new ApiResponseDto(true, true, null);
        logger.info("PUT api/v1/membership/point HTTP STATUS OK");
        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }


}
