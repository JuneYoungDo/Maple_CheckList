package com.maple.checklist.domain.member.usecase;

import com.maple.checklist.domain.member.dto.request.ChangePasswordDto;
import com.maple.checklist.domain.member.dto.request.PasswordDto;
import com.maple.checklist.domain.member.entity.Member;
import java.util.concurrent.CompletableFuture;

public interface UpdateMemberUseCase {

    void withdrawMember(Member member, PasswordDto passwordDto);
    void changePassword(Member member, ChangePasswordDto changePasswordDto);
    void logout(String authorization);
    CompletableFuture<Void> resetPassword(Member member);
}
