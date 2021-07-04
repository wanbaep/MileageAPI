package com.wanbaep.membership.controller;

import com.wanbaep.membership.dto.ApiResponseDto;
import com.wanbaep.membership.dto.MembershipPointUpdateRequestDto;
import com.wanbaep.membership.dto.MembershipResponseDto;
import com.wanbaep.membership.dto.MembershipSaveRequestDto;
import com.wanbaep.membership.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1")
public class MembershipApiController {
    private final MembershipService membershipService;

    @GetMapping("membership")
    public ResponseEntity<?> getMembershipListByUserId(
            @RequestHeader(value="X-USER-ID") String userId) {
        List<MembershipResponseDto> responseDtoList = membershipService.findByUserId(userId);
        ApiResponseDto apiResponseDto = new ApiResponseDto(true, responseDtoList, null);
        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }

    //TODO: 중복 userId membershipId 등록 불가 기능
    @PostMapping("membership")  //@RequestBody
    public ResponseEntity<?> registerMembership(
            @RequestHeader(value="X-USER-ID") String userId,
            @RequestBody MembershipSaveRequestDto requestDto) {

        MembershipResponseDto responseDto = membershipService.save(userId, requestDto);
        ApiResponseDto apiResponseDto = new ApiResponseDto(true, responseDto, null);
        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("membership/{membershipId}")
    public ResponseEntity<?> deactivateMembership(
            @RequestHeader(value="X-USER-ID") String userId,
            @PathVariable("membershipId") String membershipId) {
        ApiResponseDto apiResponseDto = new ApiResponseDto(true, true, null);
        membershipService.disable(userId, membershipId);
        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }

    @GetMapping("membership/{membershipId}")
    public ResponseEntity<?> getMembershipByIdAndByMembershipId(
            @RequestHeader(value="X-USER-ID") String userId,
            @PathVariable("membershipId") String membershipId) {
        MembershipResponseDto responseDto = membershipService.findById(userId, membershipId);
        ApiResponseDto apiResponseDto = new ApiResponseDto(true, responseDto, null);
        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }

    @PutMapping("membership/point") //@RequestBody
    public ResponseEntity<?> accumulatePoint(
            @RequestHeader(value="X-USER-ID") String userId,
            @RequestBody MembershipPointUpdateRequestDto requestDto) {
        MembershipResponseDto responseDto = membershipService.update(userId, requestDto);
        ApiResponseDto apiResponseDto = new ApiResponseDto(true, true, null);
        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }
}
