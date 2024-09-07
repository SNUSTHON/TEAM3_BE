package com.team3.core.domain.category.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateMyCategoriesRequest {
    private Set<Long> categoryIds;
}
