package com.example.repository;

import com.example.entity.BigQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BigQuestionRepository extends JpaRepository<BigQuestion, Long> {

}
