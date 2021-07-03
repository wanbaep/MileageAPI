package com.wanbaep.membership.service;

import com.wanbaep.membership.domain.Membership;
import com.wanbaep.membership.domain.MembershipRepository;
import com.wanbaep.membership.dto.MembershipPointUpdateRequestDto;
import com.wanbaep.membership.dto.MembershipResponseDto;
import com.wanbaep.membership.dto.MembershipSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class MembershipService {
    private final MembershipRepository membershipRepository;

    @Transactional
    public MembershipResponseDto save(String userId, MembershipSaveRequestDto requestDto) {
        Membership entity = membershipRepository.save(requestDto.toEntity(userId));
        return new MembershipResponseDto(entity);
    }

    @Transactional
    public MembershipResponseDto update(String userId, MembershipPointUpdateRequestDto requestDto) {
        Membership membership = membershipRepository.findByUserIdAndMembershipId(userId, requestDto.getMembershipId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. userId="+ userId));
        membership.update(requestDto.getMembershipId(), requestDto.getAmount());
        return new MembershipResponseDto(membership);
    }

    public MembershipResponseDto findById(String userId, String membershipId) {
        Membership entity = membershipRepository.findByUserIdAndMembershipId(userId, membershipId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. userId="+ userId));

        return new MembershipResponseDto(entity);
    }

}
