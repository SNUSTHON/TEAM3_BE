package com.team3.core.domain.category.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateMyCategoriesRequest {
    private Set<Long> categoryIds = new HashSet<>();
}
