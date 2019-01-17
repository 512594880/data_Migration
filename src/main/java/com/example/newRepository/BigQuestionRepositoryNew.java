package com.example.newRepository;

import com.example.newEntity.BigQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BigQuestionRepositoryNew extends JpaRepository<BigQuestion, Long> {

    List<BigQuestion> findByIdIn(Set<Long> bigQuestionIds);

    List<BigQuestion> findByName(String name);

    List<BigQuestion> findByNameAndCreatedDate(String name);
}
