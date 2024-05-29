package com.maple.checklist.domain.member.controller;

import com.maple.checklist.domain.member.dto.request.EmailDto;
import com.maple.checklist.domain.member.dto.request.LoginDto;
import com.maple.checklist.domain.member.dto.request.MemberBaseDto;
import com.maple.checklist.domain.member.dto.request.ValidateEmailDto;
import com.maple.checklist.domain.member.usecase.RegisterMemberUseCase;
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
public class RegisterMemberController {

    private final RegisterMemberUseCase registerMemberUseCase;

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginDto loginDto) {
        return new ResponseEntity(registerMemberUseCase.login(loginDto), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity registerMember(@Valid @RequestBody MemberBaseDto memberBaseDto) {
        registerMemberUseCase.registerMember(memberBaseDto);
        return new ResponseEntity(200, HttpStatus.OK);
    }

    @PostMapping("/send-email")
    public ResponseEntity sendEmail(@Valid @RequestBody EmailDto emailDto) {
        registerMemberUseCase.sendValidateEmail(emailDto.getEmail());
        return new ResponseEntity(200, HttpStatus.OK);
    }

    @PostMapping("/verify-email")
    public ResponseEntity verifyEmail(@Valid @RequestBody ValidateEmailDto validateEmailDto) {
        registerMemberUseCase.validateEmail(validateEmailDto);
        return new ResponseEntity(200, HttpStatus.OK);
    }
}
