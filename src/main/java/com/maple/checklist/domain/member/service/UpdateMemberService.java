package com.maple.checklist.domain.member.service;

import com.maple.checklist.domain.member.dto.request.ChangePasswordDto;
import com.maple.checklist.domain.member.dto.request.PasswordDto;
import com.maple.checklist.domain.member.entity.Member;
import com.maple.checklist.domain.member.repository.MemberRepository;
import com.maple.checklist.domain.member.usecase.UpdateMemberUseCase;
import com.maple.checklist.global.config.exception.BaseException;
import com.maple.checklist.global.config.exception.errorCode.AuthErrorCode;
import com.maple.checklist.global.utils.BcryptUtilsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class UpdateMemberService implements UpdateMemberUseCase {

    private final BcryptUtilsService bcryptUtilsService;
    private final MemberRepository memberRepository;

    private void save(Member member) {
        memberRepository.save(member);
    }

    @Override
    public void withdrawMember(Member member, PasswordDto passwordDto) {
        if (bcryptUtilsService.isMatch(passwordDto.getPassword(), member.getPassword())) {
            member.setDeleted();
            save(member);
        } else {
            throw new BaseException(AuthErrorCode.INCORRECT_PASSWORD);
        }
    }

    @Override
    public void changePassword(Member member, ChangePasswordDto changePasswordDto) {
        if (bcryptUtilsService.isMatch(changePasswordDto.getPassword(), member.getPassword())) {
            member.changePassword(
                bcryptUtilsService.encrypt(changePasswordDto.getChangePassword()));
            save(member);
        } else {
            throw new BaseException(AuthErrorCode.INCORRECT_PASSWORD);
        }
    }
}
