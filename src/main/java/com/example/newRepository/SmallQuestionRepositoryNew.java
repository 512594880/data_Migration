package com.example.newRepository;

import com.example.entity.SmallQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SmallQuestionRepositoryNew extends JpaRepository<SmallQuestion,Long> {
    List<SmallQuestion> findByBigQuestionId(Long id);
}
