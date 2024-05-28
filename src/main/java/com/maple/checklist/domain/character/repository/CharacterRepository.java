package com.maple.checklist.domain.character.repository;

import com.maple.checklist.domain.character.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<Character,Long> {
}
