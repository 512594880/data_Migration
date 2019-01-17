package com.example.repository;

import com.example.entity.PageLayoutBigQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PageLayoutBigQuestionRepository extends JpaRepository<PageLayoutBigQuestion,Long> {
    List<PageLayoutBigQuestion> findByPageLayoutIdIn(Set<Long> pageLayoutIds);
}
