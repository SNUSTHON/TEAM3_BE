package com.team3.core.domain.challenge.dao;

import com.team3.core.domain.challenge.domain.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    List<Challenge> findAllByCategory_IdAndLevel(Long categoryId, int level);
}
