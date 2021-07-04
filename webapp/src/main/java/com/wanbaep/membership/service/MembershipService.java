package com.wanbaep.membership.service;

import com.wanbaep.membership.domain.Membership;
import com.wanbaep.membership.domain.MembershipRepository;
import com.wanbaep.membership.dto.MembershipPointUpdateRequestDto;
import com.wanbaep.membership.dto.MembershipResponseDto;
import com.wanbaep.membership.dto.MembershipSaveRequestDto;
import com.wanbaep.membership.exception.DuplicateMembershipException;
import com.wanbaep.membership.exception.MembershipDisabledException;
import com.wanbaep.membership.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MembershipService {
    private final MembershipRepository membershipRepository;
    Logger logger = LoggerFactory.getLogger(MembershipService.class);


    public List<MembershipResponseDto> findByUserId(String userId) {
        List<Membership> entityList = membershipRepository.findByUserId(userId);
        logger.debug("findByUserId size({})", entityList.size());
        List<MembershipResponseDto> responseDtoList = new LinkedList<>();
        for(Membership entity : entityList) {
            responseDtoList.add(new MembershipResponseDto(entity));
        }
        return responseDtoList;
    }

    @Transactional
    public MembershipResponseDto save(String userId, MembershipSaveRequestDto requestDto) {
        Membership entity = membershipRepository.findByUserIdAndMembershipId(userId, requestDto.getMembershipId()).orElse(null);
        if(entity != null) {
            logger.info("Already Registered Membership. userId({}), membershipId({})", userId, requestDto.getMembershipId());
            if(entity.isEnabled()) {
                logger.error("Duplicate Membership Found. userId({}), membershipId({})", userId, requestDto.getMembershipName());
                throw new DuplicateMembershipException("Duplicated membership, userId(" + userId + "), membershipId("+ requestDto.getMembershipId()+")");
            } else {
                logger.info("Enable Membership. userId({}), membershipId({})", userId, requestDto.getMembershipId());
                entity.enable(requestDto.getMembershipName(), requestDto.getPoint());
            }
        } else {
            entity = membershipRepository.save(requestDto.toEntity(userId));
            logger.info("Save New Membership Success. userId({}), membershipId({})", userId, requestDto.getMembershipId());
        }
        return new MembershipResponseDto(entity);
    }

    @Transactional
    public void disable(String userId, String membershipId) {
        Membership entity = membershipRepository.findByUserIdAndMembershipId(userId, membershipId)
                .orElseThrow(() -> new UserNotFoundException("Can not find userId("+userId+"), membershipId("+membershipId+")"));
        entity.disable();
        logger.info("Disable Membership Success. userId({}), membershipId({})", userId, membershipId);
    }

    public MembershipResponseDto findByUserIdAndMembershipId(String userId, String membershipId) {
        Membership entity = membershipRepository.findByUserIdAndMembershipId(userId, membershipId)
                .orElseThrow(() -> new UserNotFoundException("Can not find userId("+userId+"), membershipId("+membershipId+")"));
        return new MembershipResponseDto(entity);
    }

    @Transactional
    public MembershipResponseDto update(String userId, MembershipPointUpdateRequestDto requestDto) {
        Membership membership = membershipRepository.findByUserIdAndMembershipId(userId, requestDto.getMembershipId())
                .orElseThrow(() -> new UserNotFoundException("Can not find userId("+userId+"), membershipId("+requestDto.getMembershipId()+")"));
        if(membership.isEnabled()) {
            membership.update(requestDto.getAmount());
            logger.info("Accumulate Membership Point Success. userId({}), membershipId({}), point({})", userId, requestDto.getMembershipId(), membership.getPoint());
        } else {
            throw new MembershipDisabledException("Disabled Membership");
        }
        return new MembershipResponseDto(membership);
    }
}
