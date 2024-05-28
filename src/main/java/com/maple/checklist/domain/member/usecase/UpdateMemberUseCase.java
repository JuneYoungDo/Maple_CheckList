package com.maple.checklist.domain.member.usecase;

import com.maple.checklist.domain.member.dto.request.ChangePasswordDto;
import com.maple.checklist.domain.member.dto.request.PasswordDto;
import com.maple.checklist.domain.member.entity.Member;

public interface UpdateMemberUseCase {

    void withdrawMember(Member member, PasswordDto passwordDto);
    void changePassword(Member member, ChangePasswordDto changePasswordDto);
}
