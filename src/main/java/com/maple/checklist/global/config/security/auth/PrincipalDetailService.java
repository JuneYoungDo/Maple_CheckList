package com.maple.checklist.global.config.security.auth;

import com.maple.checklist.domain.member.entity.Member;
import com.maple.checklist.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public PrincipalDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findMemberByMemberIdAndDeleted(Long.parseLong(username))
            .orElseThrow(() -> new UsernameNotFoundException("없는 회원입니다."));

        return new PrincipalDetails(member);
    }
}
