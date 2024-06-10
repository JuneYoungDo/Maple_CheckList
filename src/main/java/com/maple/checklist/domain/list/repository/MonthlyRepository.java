package com.maple.checklist.domain.list.repository;

import com.maple.checklist.domain.character.entity.Character;
import com.maple.checklist.domain.list.entity.Monthly;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MonthlyRepository extends JpaRepository<Monthly, Long> {

    @Query(value = "select m from Monthly m where m.character = :character and m.deleted = false")
    Optional<List<Monthly>> findAllByCharacterAndDeleted(Character character);

    @Query(value = "select m from Monthly m where m.monthlyId = :monthlyId and m.deleted = false")
    Optional<Monthly> findMonthlyByMonthlyIdAndDeleted(Long monthlyId);

    @Query(value = "select m from Monthly m where m.deleted = false")
    Optional<List<Monthly>> findAllByDeleted();
}
