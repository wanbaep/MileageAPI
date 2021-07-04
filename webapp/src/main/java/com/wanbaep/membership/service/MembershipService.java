package com.wanbaep.membership.service;

import com.wanbaep.membership.domain.Membership;
import com.wanbaep.membership.domain.MembershipRepository;
import com.wanbaep.membership.dto.MembershipPointUpdateRequestDto;
import com.wanbaep.membership.dto.MembershipResponseDto;
import com.wanbaep.membership.dto.MembershipSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MembershipService {
    private final MembershipRepository membershipRepository;

    public List<MembershipResponseDto> findByUserId(String userId) {
        List<Membership> entityList = membershipRepository.findByUserId(userId);
        List<MembershipResponseDto> responseDtoList = new LinkedList<>();
        for(Membership entity : entityList) {
            responseDtoList.add(new MembershipResponseDto(entity));
        }
        return responseDtoList;
    }

    @Transactional
    public MembershipResponseDto save(String userId, MembershipSaveRequestDto requestDto) {
        //TODO: userId & membershipId 같은 것이 있는지 읽어서 확인..?
//        Membership entity = membershipRepository.findByUserIdAndMembershipId(userId, requestDto.getMembershipId())
//                .orElseThrow(() -> new IllegalArgumentException("해당 사용자의 멤버십 아이디가 없습니다. userId=" + userId + " membershipId="+ membershipId));
        Membership entity = membershipRepository.save(requestDto.toEntity(userId));
        return new MembershipResponseDto(entity);
    }

    @Transactional
    public void disable(String userId, String membershipId) {
        Membership entity = membershipRepository.findByUserIdAndMembershipId(userId, membershipId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자의 멤버십 아이디가 없습니다. userId=" + userId + " membershipId="+ membershipId));
        entity.disable();
    }

    public MembershipResponseDto findById(String userId, String membershipId) {
        Membership entity = membershipRepository.findByUserIdAndMembershipId(userId, membershipId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자의 멤버십 아이디가 없습니다. userId=" + userId + " membershipId="+ membershipId));
        return new MembershipResponseDto(entity);
    }

    @Transactional
    public MembershipResponseDto update(String userId, MembershipPointUpdateRequestDto requestDto) {
        Membership membership = membershipRepository.findByUserIdAndMembershipId(userId, requestDto.getMembershipId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. userId="+ userId));
        membership.update(requestDto.getMembershipId(), requestDto.getAmount());
        return new MembershipResponseDto(membership);
    }
}
