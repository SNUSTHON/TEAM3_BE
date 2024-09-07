package com.team3.core.domain.challenge.domain;

import com.team3.core.domain.category.domain.Category;
import com.team3.core.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Challenge extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    private int duration;

    @Builder
    public Challenge(Category category, String name, int level, int duration) {
        this.category = category;
        this.name = name;
        this.level = level;
        this.duration = duration;
    }

}
