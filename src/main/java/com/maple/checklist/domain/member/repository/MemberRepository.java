package com.maple.checklist.domain.member.repository;

import com.maple.checklist.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(value = "select m from Member m where m.email = :email and m.deleted = false")
    Optional<Member> findMemberByEmailAndDeleted(String email);

    Boolean existsByEmailAndDeleted(String email, boolean deleted);
}
