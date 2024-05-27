package com.maple.checklist.domain.member.service;

import com.maple.checklist.domain.member.dto.request.ChangePasswordDto;
import com.maple.checklist.domain.member.dto.request.MemberBaseDto;
import com.maple.checklist.domain.member.dto.request.PasswordDto;
import com.maple.checklist.domain.member.dto.request.ValidateEmailDto;
import com.maple.checklist.domain.member.entity.Member;
import com.maple.checklist.domain.member.entity.Role;
import com.maple.checklist.domain.member.repository.MemberRepository;
import com.maple.checklist.domain.member.usecase.RegisterMemberUseCase;
import com.maple.checklist.global.config.exception.BaseException;
import com.maple.checklist.global.config.exception.errorCode.AuthErrorCode;
import com.maple.checklist.global.utils.BcryptUtilsService;
import com.maple.checklist.global.utils.MailUtilsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class RegisterMemberService implements RegisterMemberUseCase {

    private final MemberRepository memberRepository;
    private final BcryptUtilsService bcryptUtilsService;
    private final MailUtilsService mailUtilsService;

    private void save(Member member) {
        memberRepository.save(member);
    }

    public void register(MemberBaseDto memberBaseDto) {
        Member member = Member.builder()
            .email(memberBaseDto.getEmail())
            .password(bcryptUtilsService.encrypt(memberBaseDto.getPassword()))
            .role(Role.MEMBER)
            .build();
        save(member);
    }

    @Override
    public void sendValidateEmail(String email) {
        checkAlreadyUsed(email);
        mailUtilsService.sendAuthMail(email);
    }

    @Override
    public void validateEmail(ValidateEmailDto validateEmailDto) {

    }

    @Override
    public void registerMember(MemberBaseDto memberBaseDto) {
        // 레디스에 등록되어있는 이메일인지 확인 과정 필요
        register(memberBaseDto);
    }

    @Override
    public void withdrawMember(Member member, PasswordDto passwordDto) {
        if (bcryptUtilsService.isMatch(passwordDto.getPassword(), member.getPassword())) {
            member.setDeleted();
        }
    }

    @Override
    public void changePassword(Member member, ChangePasswordDto changePasswordDto) {
        if (bcryptUtilsService.isMatch(changePasswordDto.getPassword(), member.getPassword())) {
            member.changePassword(
                bcryptUtilsService.encrypt(changePasswordDto.getChangePassword()));
        }
    }

    private void checkAlreadyUsed(String email) {
        if (memberRepository.existsByEmailAndDeleted(email, false)) {
            throw new BaseException(AuthErrorCode.ALREADY_USED_EMAIL);
        }
    }
}
