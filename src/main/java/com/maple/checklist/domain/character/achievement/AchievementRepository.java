package com.maple.checklist.domain.character.achievement;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AchievementRepository extends JpaRepository<Achievement,Long> {

    @Query(value = "select a from Achievement a where a.deleted = false")
    Optional<List<Achievement>> findAllByDeleted();
}
