package com.wanbaep.membership.controller;

import com.wanbaep.membership.dto.MembershipSaveRequestDto;
import com.wanbaep.membership.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1")
public class MembershipApiController {
    @Resource(name= "membershipService")
    private final MembershipService membershipService;

    @GetMapping("membership")
    public ResponseEntity<?> getMembership() {
        return new ResponseEntity<>("GET", HttpStatus.OK);
    }

    @PostMapping("membership")  //@RequestBody
    public Long registerMembership(@RequestBody MembershipSaveRequestDto requestDto) {
//        return new ResponseEntity<>("POST", HttpStatus.OK);
        return membershipService.save(requestDto);
    }

    @DeleteMapping("membership/{membershipId}")
    public ResponseEntity<?> deactivateMembership(@PathVariable("membershipId") String membershipId) {
        return new ResponseEntity<>("DELETE", HttpStatus.OK);
    }

    @GetMapping("membership/{membershipId}")
    public ResponseEntity<?> retrieveMembershipById(@PathVariable("membershipId") String membershipId) {
        return new ResponseEntity<>("GET", HttpStatus.OK);
    }

    @PutMapping("membership/point") //@RequestBody
    public ResponseEntity<?> accumulatePoint() {
        return new ResponseEntity<>("PUT", HttpStatus.OK);
    }
}
