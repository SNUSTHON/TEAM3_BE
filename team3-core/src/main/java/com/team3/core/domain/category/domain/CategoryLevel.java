package com.team3.core.domain.category.domain;

import com.team3.core.domain.member.domain.Member;
import com.team3.core.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import static com.team3.core.global.common.Constants.MAX_CNT;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryLevel extends BaseEntity {
    @Column(nullable = false)
    @ColumnDefault("1")
    private int level = 1;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int cnt = 0;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isSelected = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;

    @Builder
    public CategoryLevel(int level, int cnt, boolean isSelected, Member member, Category category) {
        this.level = level;
        this.cnt = cnt;
        this.isSelected = isSelected;
        this.member = member;
        this.category = category;
    }

    public void changeSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void plusCnt() {
        this.cnt++;

        if (cnt >= MAX_CNT) {
            this.level++;
            this.cnt = 0;
        }
    }

    public void minusCnt() {
        if (cnt <= 0) {
            this.level--;
            this.cnt = MAX_CNT - 1;
        } else {
            this.cnt--;
        }
    }
}
