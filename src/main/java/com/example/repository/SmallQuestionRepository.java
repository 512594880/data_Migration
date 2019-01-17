package com.example.repository;

import com.example.entity.SmallQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmallQuestionRepository extends JpaRepository<SmallQuestion,Long> {
}
