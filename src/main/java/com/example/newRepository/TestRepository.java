package com.example.newRepository;

import com.example.entity.BigQuestion;
import com.example.newEntity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface TestRepository extends JpaRepository<Test, Long> {

}
