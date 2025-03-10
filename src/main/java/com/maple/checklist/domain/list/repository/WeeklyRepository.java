package com.maple.checklist.domain.list.repository;

import com.maple.checklist.domain.character.entity.Character;
import com.maple.checklist.domain.list.entity.Weekly;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WeeklyRepository extends JpaRepository<Weekly, Long> {

    @Query(value = "select w from Weekly w where w.character = :character and w.deleted = false")
    Optional<List<Weekly>> findAllByCharacterAndDeleted(Character character);

    @Query(value = "select w from Weekly w where w.weeklyId = :weeklyId and w.deleted = false")
    Optional<Weekly> findWeeklyByWeeklyIdAndDeleted(Long weeklyId);

    @Query(value = "select w from Weekly w where w.deleted = false")
    Optional<List<Weekly>> findAllByDeleted();

    @Query(value = "select w from Weekly w where w.deleted = true")
    Optional<List<Weekly>> findAllByDeletedTrue();
}
