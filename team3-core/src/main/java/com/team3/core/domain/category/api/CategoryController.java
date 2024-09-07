package com.team3.core.domain.category.api;


import com.team3.core.domain.category.applicatioin.CategoryService;
import com.team3.core.domain.category.dto.MyCategoryResponse;
import com.team3.core.global.auth.model.Team3OAuth2User;
import com.team3.core.global.config.SwaggerConfig;
import com.team3.core.global.response.StandardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Tag(name = "카테고리", description = "카테고리 관련 API")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping("/my")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "나의 카테고리 조회 성공",
                            useReturnTypeSchema = true
                    )
            }
    )
    @Operation(summary = "나의 카테고리 조회", description = "나의 카테고리 조회 API입니다.",
            security = @SecurityRequirement(name = SwaggerConfig.JWT_SECURITY_SCHEME)
    )

    public StandardResponse<List<MyCategoryResponse>> getMyCategories(@AuthenticationPrincipal Team3OAuth2User team3OAuth2User) {
        return StandardResponse.success(categoryService.getMyCategories(team3OAuth2User.getMember().getId()));
    }

    @PostMapping("/my")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "나의 도전 카테고리 변경 성공",
                            useReturnTypeSchema = true
                    )
            }
    )
    @Operation(summary = "나의 도전 카테고리 변경", description = "나의 도전 카테고리 변경 API입니다.",
            security = @SecurityRequirement(name = SwaggerConfig.JWT_SECURITY_SCHEME)
    )
    public StandardResponse<String> updateMyCategories(@AuthenticationPrincipal Team3OAuth2User team3OAuth2User, @RequestBody Set<Long> categoryIds) {
        categoryService.updateMyCategories(team3OAuth2User.getId(), categoryIds);

        return StandardResponse.success("나의 도전 카테고리 변경에 성공하였습니다.");
    }

}
