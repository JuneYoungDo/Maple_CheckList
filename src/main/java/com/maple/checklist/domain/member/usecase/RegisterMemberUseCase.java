package com.maple.checklist.domain.member.usecase;

import com.maple.checklist.domain.member.dto.request.ChangePasswordDto;
import com.maple.checklist.domain.member.dto.request.MemberBaseDto;
import com.maple.checklist.domain.member.dto.request.PasswordDto;
import com.maple.checklist.domain.member.dto.request.ValidateEmailDto;
import com.maple.checklist.domain.member.entity.Member;

public interface RegisterMemberUseCase {
    void sendValidateEmail(String email);
    void validateEmail(ValidateEmailDto validateEmailDto);
    void registerMember(MemberBaseDto memberBaseDto);
    void withdrawMember(Member member, PasswordDto passwordDto);
    void changePassword(Member member, ChangePasswordDto changePasswordDto);
}
