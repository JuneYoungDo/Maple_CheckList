package com.maple.checklist.domain.character.job;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JobRepository extends JpaRepository<Job,Long> {

    @Query(value = "select j from Job j where j.name = :name")
    Optional<Job> findJobByName(String name);

    @Query(value = "select j from Job j where j.jobId = 1L")
    Job findDefaultJob();
}
