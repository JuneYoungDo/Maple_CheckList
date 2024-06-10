package com.maple.checklist.domain.character.characterJob;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CharacterJobRepository extends JpaRepository<CharacterJob,Long> {

    @Query(value = "select j from CharacterJob j where j.name = :name")
    Optional<CharacterJob> findJobByName(String name);

    @Query(value = "select j from CharacterJob j where j.jobId = 1L")
    CharacterJob findDefaultJob();
}
