package com.wanbaep.membership.service;

import com.wanbaep.membership.domain.MembershipRepository;
import com.wanbaep.membership.dto.MembershipSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class MembershipService {
    private final MembershipRepository membershipRepository;

    @Transactional
    public Long save(MembershipSaveRequestDto requestDto) {
        return membershipRepository.save(requestDto.toEntity()).getSeq();
    }
}
