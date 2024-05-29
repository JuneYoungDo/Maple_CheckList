package com.maple.checklist.domain.character.repository;

import com.maple.checklist.domain.character.entity.Character;
import com.maple.checklist.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CharacterRepository extends JpaRepository<Character,Long> {

    @Query(value = "select c from Character c where c.member = :member and c.deleted = false")
    Optional<List<Character>> findCharactersByMember(Member member);

    @Query(value = "select c from Character c where c.characterId = :characterId and c.deleted = false")
    Optional<Character> findCharacterByCharacterIdAndDeleted(Long characterId);
}
