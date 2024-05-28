package com.maple.checklist.domain.member.controller;

import com.maple.checklist.domain.member.dto.request.ChangePasswordDto;
import com.maple.checklist.domain.member.dto.request.EmailDto;
import com.maple.checklist.domain.member.dto.request.PasswordDto;
import com.maple.checklist.domain.member.entity.Member;
import com.maple.checklist.domain.member.usecase.UpdateMemberUseCase;
import com.maple.checklist.global.config.security.auth.CurrentMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class UpdateMemberController {

    private final UpdateMemberUseCase updateMemberUseCase;


    @PostMapping("/reset-pwd")
    public ResponseEntity resetPwd(@Valid @RequestBody EmailDto emailDto) {

        return null;
    }

    @PatchMapping("/change-pwd")
    public ResponseEntity changePwd(@CurrentMember Member member,
        @Valid @RequestBody ChangePasswordDto changePasswordDto) {
        updateMemberUseCase.changePassword(member, changePasswordDto);
        return new ResponseEntity(200, HttpStatus.OK);
    }


    @DeleteMapping("/withdraw")
    public ResponseEntity deleteMember(@CurrentMember Member member,
        @Valid @RequestBody PasswordDto passwordDto) {
        updateMemberUseCase.withdrawMember(member, passwordDto);
        return new ResponseEntity(200, HttpStatus.OK);
    }
}
