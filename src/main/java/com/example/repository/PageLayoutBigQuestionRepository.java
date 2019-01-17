package com.example.repository;

import com.example.entity.PageLayoutBigQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageLayoutBigQuestionRepository extends JpaRepository<PageLayoutBigQuestion,Long> {
}
