package com.maple.checklist.domain.member.usecase;

import com.maple.checklist.domain.member.dto.request.LoginDto;
import com.maple.checklist.domain.member.dto.request.MemberBaseDto;
import com.maple.checklist.domain.member.dto.request.ValidateEmailDto;
import com.maple.checklist.domain.member.dto.response.LoginResponseDto;

public interface RegisterMemberUseCase {
    LoginResponseDto login(LoginDto loginDto);
    void sendValidateEmail(String email);
    void validateEmail(ValidateEmailDto validateEmailDto);
    void registerMember(MemberBaseDto memberBaseDto);
}
