package com.cmdlee.quizsushi.quiz.domain.repository;

import com.cmdlee.quizsushi.quiz.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
