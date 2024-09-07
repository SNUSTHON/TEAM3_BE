package com.team3.core.domain.category.applicatioin;

import com.team3.core.domain.category.dao.CategoryLevelRepository;
import com.team3.core.domain.category.domain.CategoryLevel;
import com.team3.core.domain.category.dto.MyCategoryResponse;
import com.team3.core.domain.member.dao.MemberRepository;
import com.team3.core.domain.member.domain.Member;
import com.team3.core.global.exception.ErrorCode;
import com.team3.core.global.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryLevelService {

    private final MemberRepository memberRepository;
    private final CategoryLevelRepository categoryLevelRepository;

    // 나의 카테고리 조회
    @Transactional(readOnly = true)
    public List<MyCategoryResponse> getMyCategories(Long userId) {
        List<CategoryLevel> categoryLevels = getCategoryLevels(userId);

        return categoryLevels.stream()
                .map(MyCategoryResponse::new)
                .collect(Collectors.toList());
    }

    // 나의 도전 카테고리 변경
    public void updateMyCategories(Long userId, Set<Long> categoryIds) {
        List<CategoryLevel> categoryLevels = getCategoryLevels(userId);

        categoryLevels.forEach(categoryLevel -> {
                    if (categoryIds.contains(categoryLevel.getCategory().getId())) {
                        if (!categoryLevel.isSelected()) {
                            categoryLevel.changeSelected(true);
                        }
                    }
                    else {
                        if (categoryLevel.isSelected()) {
                            categoryLevel.changeSelected(false);
                        }
                    }
                });

    }

    // 멤버의 전체 카테고리 레벨을 조회
    private List<CategoryLevel> getCategoryLevels(Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));
        return categoryLevelRepository.findAllByMember(member);
    }

}
