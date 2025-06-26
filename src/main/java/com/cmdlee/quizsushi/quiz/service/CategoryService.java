package com.cmdlee.quizsushi.service;

import com.cmdlee.quizsushi.domain.dto.response.CategoryResponse;
import com.cmdlee.quizsushi.domain.dto.response.IntroductionCategoryResponse;
import com.cmdlee.quizsushi.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::from)
                .toList();
    }

    public List<IntroductionCategoryResponse> findIntroductionCategories() {
        return categoryRepository.findAll().stream()
                .map(IntroductionCategoryResponse::from)
                .toList();
    }
}
