package com.wanbaep.webapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class MileageController {

    @GetMapping("membership")
    public ResponseEntity<?> getMembership() {
        return new ResponseEntity<>("GET", HttpStatus.OK);
    }

    @PostMapping("membership")  //@RequestBody
    public ResponseEntity<?> registerMembership() {
        return new ResponseEntity<>("POST", HttpStatus.OK);
    }

    @PatchMapping("membership/{membershipId}")
    public ResponseEntity<?> deactivateMembership(@PathVariable("membershipId") String membershipId) {
        return new ResponseEntity<>("PATCH", HttpStatus.OK);
    }

    @GetMapping("membership/{membershipId}")
    public ResponseEntity<?> retrieveMembershipById(@PathVariable("membershipId") String membershipId) {
        return new ResponseEntity<>("GET", HttpStatus.OK);
    }

    @PatchMapping("membership/point") //@RequestBody
    public ResponseEntity<?> acumulatePoint() {
        return new ResponseEntity<>("PATCH", HttpStatus.OK);
    }
}
