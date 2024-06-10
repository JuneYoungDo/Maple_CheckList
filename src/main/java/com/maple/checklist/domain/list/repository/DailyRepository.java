package com.maple.checklist.domain.list.repository;

import com.maple.checklist.domain.character.entity.Character;
import com.maple.checklist.domain.list.entity.Daily;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DailyRepository extends JpaRepository<Daily, Long> {

    @Query(value = "select d from Daily d where d.character = :character and d.deleted = false")
    Optional<List<Daily>> findAllByCharacterAndDeleted(Character character);

    @Query(value = "select d from Daily d where d.dailyId = :dailyId and d.deleted = false")
    Optional<Daily> findDailyByDailyIdAndDeleted(Long dailyId);

    @Query(value = "select d from Daily d where d.deleted = false")
    Optional<List<Daily>> findAllByDeleted();
}
