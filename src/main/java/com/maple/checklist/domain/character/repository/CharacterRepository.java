package com.maple.checklist.domain.character.repository;

import com.maple.checklist.domain.character.entity.Character;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CharacterRepository extends JpaRepository<Character,Long> {

    @Query(value = "select c from Character c where c.characterId = :characterId and c.deleted = false")
    Optional<Character> findCharacterByCharacterIdAndDeleted(Long characterId);
}
