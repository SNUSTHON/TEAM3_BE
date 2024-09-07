package com.team3.core.domain.category.dto;

import com.team3.core.domain.category.domain.CategoryLevel;
import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyCategoryResponse {

    private Long categoryId;
    private String categoryName;
    private boolean isSelected;

    @Builder
    public MyCategoryResponse(CategoryLevel categoryLevel) {
        this.categoryId = categoryLevel.getCategory().getId();
        this.categoryName = categoryLevel.getCategory().getName();
        this.isSelected = categoryLevel.isSelected();
    }
}
