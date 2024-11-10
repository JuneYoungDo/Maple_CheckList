package com.maple.checklist.domain.member.service;

import com.maple.checklist.domain.member.dto.request.ChangePasswordDto;
import com.maple.checklist.domain.member.dto.request.PasswordDto;
import com.maple.checklist.domain.member.entity.Member;
import com.maple.checklist.domain.member.repository.MemberRepository;
import com.maple.checklist.domain.member.usecase.UpdateMemberUseCase;
import com.maple.checklist.global.config.exception.BaseException;
import com.maple.checklist.global.config.exception.errorCode.AuthErrorCode;
import com.maple.checklist.global.config.security.jwt.JwtTokenProvider;
import com.maple.checklist.global.utils.BcryptUtilsService;
import com.maple.checklist.global.utils.MailUtilsService;
import com.maple.checklist.global.utils.RedisService;
import jakarta.transaction.Transactional;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class UpdateMemberService implements UpdateMemberUseCase {

    private final BcryptUtilsService bcryptUtilsService;
    private final MemberRepository memberRepository;
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MailUtilsService mailUtilsService;

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

    @Override
    public void logout(String authorization) {
        long remainTime = jwtTokenProvider.getRemainingValidityInSeconds(authorization);
        if (remainTime == -1) {
            return;
        } else {
            redisService.saveLogoutToken(authorization, remainTime);
        }
    }

    @Override
    public void resetPassword(Member member) {
        CompletableFuture.runAsync(() -> {
            String code = mailUtilsService.sendResetMail(member.getEmail());
            member.changePassword(bcryptUtilsService.encrypt(code));
            save(member);
        });
    }
}
