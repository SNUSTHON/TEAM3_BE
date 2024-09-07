package com.team3.core.domain.category.dao;

import com.team3.core.domain.category.domain.CategoryLevel;
import com.team3.core.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryLevelRepository extends JpaRepository<CategoryLevel, Long> {
    List<CategoryLevel> findAllByMember(Member member);

}
