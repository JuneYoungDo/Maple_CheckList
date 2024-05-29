package com.maple.checklist.domain.member.service;

import com.maple.checklist.domain.member.dto.request.LoginDto;
import com.maple.checklist.domain.member.dto.request.MemberBaseDto;
import com.maple.checklist.domain.member.dto.request.ValidateEmailDto;
import com.maple.checklist.domain.member.dto.response.LoginResponseDto;
import com.maple.checklist.domain.member.entity.Member;
import com.maple.checklist.domain.member.entity.Role;
import com.maple.checklist.domain.member.repository.MemberRepository;
import com.maple.checklist.domain.member.usecase.RegisterMemberUseCase;
import com.maple.checklist.global.config.exception.BaseException;
import com.maple.checklist.global.config.exception.errorCode.AuthErrorCode;
import com.maple.checklist.global.config.security.jwt.JwtTokenProvider;
import com.maple.checklist.global.utils.BcryptUtilsService;
import com.maple.checklist.global.utils.MailUtilsService;
import com.maple.checklist.global.utils.RedisService;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

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
    public LoginResponseDto login(LoginDto loginDto) {
        Member member = memberRepository.findMemberByEmailAndDeleted(loginDto.getEmail())
            .orElseThrow(() -> new BaseException(AuthErrorCode.INCORRECT_EMAIL));
        if(!bcryptUtilsService.isMatch(loginDto.getPassword(),member.getPassword())) {
            throw new BaseException(AuthErrorCode.INCORRECT_PASSWORD);
        }
        return new LoginResponseDto(jwtTokenProvider.generateAccessToken(member.getMemberId()));
    }

    @Override
    public void sendValidateEmail(String email) {
        checkAlreadyUsed(email);
        String code = mailUtilsService.sendAuthMail(email);
        redisService.saveEmailVerificationToken(email,code,10);
    }

    @Override
    public void validateEmail(ValidateEmailDto validateEmailDto) {
        String code = redisService.getEmailVerificationToken(validateEmailDto.getEmail());
        if(code != null && code.equals(validateEmailDto.getCode())) {
            redisService.deleteEmailVerificationToken(validateEmailDto.getEmail());
            redisService.saveEmailVerification(validateEmailDto.getEmail(), 10);
        } else {
            throw new BaseException(AuthErrorCode.INCORRECT_CODE);
        }
    }

    @Override
    public void registerMember(MemberBaseDto memberBaseDto) {
        Boolean flag = redisService.getEmailVerification(memberBaseDto.getEmail());
        if(flag != null && flag){
            register(memberBaseDto);
            redisService.deleteEmailVerification(memberBaseDto.getEmail());
        } else {
            throw new BaseException(AuthErrorCode.INVALID_EMAIL);
        }

    }

    private void checkAlreadyUsed(String email) {
        if (memberRepository.existsByEmailAndDeleted(email, false)) {
            throw new BaseException(AuthErrorCode.ALREADY_USED_EMAIL);
        }
    }
}
