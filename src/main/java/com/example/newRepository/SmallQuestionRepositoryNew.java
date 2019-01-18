package com.example.newRepository;

import com.example.newEntity.SmallQuestionNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SmallQuestionRepositoryNew extends JpaRepository<SmallQuestionNew,Long> {
    List<SmallQuestionNew> findByBigQuestionId(Long id);

    @Query(value  ="SELECT max(id) from small_question",nativeQuery = true)
    Long findMaxId();
}
