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
    public MyCategoryResponse(Long categoryId, String categoryName, boolean isSelected) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.isSelected = isSelected;
    }

    public static MyCategoryResponse from(CategoryLevel categoryLevel) {
        return MyCategoryResponse.builder()
                .categoryId(categoryLevel.getCategory().getId())
                .categoryName(categoryLevel.getCategory().getName())
                .isSelected(categoryLevel.isSelected())
                .build();
    }
}
