package com.wanbaep.membership.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Optional<Membership> findByUserIdAndMembershipId(String userId, String MembershipId);
    List<Membership> findByUserId(String userId);
}
