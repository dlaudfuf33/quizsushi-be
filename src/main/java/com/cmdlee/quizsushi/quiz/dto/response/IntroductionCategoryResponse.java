package com.cmdlee.quizsushi.domain.dto.response;

import com.cmdlee.quizsushi.domain.model.Category;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class IntroductionCategoryResponse {
    private long id;
    private String title;
    private String description;
    private Long count;
    private String icon;

    public static IntroductionCategoryResponse from(Category category) {
        return IntroductionCategoryResponse.builder()
                .id(category.getId())
                .title(category.getTitle())
                .description(category.getDescription())
                .count(category.getCount())
                .icon(category.getIcon())
                .build();
    }
}
