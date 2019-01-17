package com.example.newRepository;

import com.example.entity.BigQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<BigQuestion, Long> {

}
