package com.example.newRepository;

import com.example.newEntity.BigQuestionNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface BigQuestionRepositoryNew extends JpaRepository<BigQuestionNew, Long> {

    List<BigQuestionNew> findByIdIn(Set<Long> bigQuestionIds);

    List<BigQuestionNew> findByName(String name);

    List<BigQuestionNew> findByNameAndCreatedDate(String name, Date date);

    @Query(value ="SELECT max(id) from big_question", nativeQuery = true)
    Long findMaxId();
}
