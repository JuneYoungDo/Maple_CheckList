package com.maple.checklist.domain.member.controller;

import com.maple.checklist.domain.member.dto.request.ChangePasswordDto;
import com.maple.checklist.domain.member.dto.request.EmailDto;
import com.maple.checklist.domain.member.dto.request.MemberBaseDto;
import com.maple.checklist.domain.member.dto.request.PasswordDto;
import com.maple.checklist.domain.member.entity.Member;
import com.maple.checklist.domain.member.usecase.RegisterMemberUseCase;
import com.maple.checklist.global.config.security.auth.CurrentMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberController {

    private final RegisterMemberUseCase registerMemberUseCase;

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody MemberBaseDto memberBaseDto) {

        return null;
    }

    @PostMapping("/register")
    public ResponseEntity registerMember(@Valid @RequestBody MemberBaseDto memberBaseDto) {
        registerMemberUseCase.registerMember(memberBaseDto);
        return new ResponseEntity(200, HttpStatus.OK);
    }

    @PostMapping("/reset-pwd")
    public ResponseEntity resetPwd(@Valid @RequestBody EmailDto emailDto) {

        return null;
    }

    @PostMapping("/change-pwd")
    public ResponseEntity changePwd(@CurrentMember Member member,
        @Valid @RequestBody ChangePasswordDto changePasswordDto) {
        registerMemberUseCase.changePassword(member, changePasswordDto);
        return new ResponseEntity(200, HttpStatus.OK);
    }

    @PostMapping("/send-email")
    public ResponseEntity sendEmail(@Valid @RequestBody EmailDto emailDto) {
        registerMemberUseCase.sendValidateEmail(emailDto.getEmail());
        return new ResponseEntity(200, HttpStatus.OK);
    }

    @PostMapping("/verify-email")
    public ResponseEntity verifyEmail() {

        return null;
    }

    @PostMapping("/member/withdraw")
    public ResponseEntity deleteMember(@CurrentMember Member member,
        @Valid @RequestBody PasswordDto passwordDto) {
        registerMemberUseCase.withdrawMember(member, passwordDto);
        return new ResponseEntity(200, HttpStatus.OK);
    }
}
