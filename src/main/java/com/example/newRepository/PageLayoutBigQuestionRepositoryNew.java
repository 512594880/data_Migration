package com.example.newRepository;

import com.example.newEntity.PageLayoutBigQuestionNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PageLayoutBigQuestionRepositoryNew extends JpaRepository<PageLayoutBigQuestionNew,Long> {
    List<PageLayoutBigQuestionNew> findByPageLayoutIdIn(Set<Long> pageLayoutIds);
}
