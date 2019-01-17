package com.example.repository;

import com.example.entity.BigQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.beans.beancontext.BeanContextMembershipEvent;
import java.util.Dictionary;
import java.util.List;
import java.util.Set;

@Repository
public interface BigQuestionRepository extends JpaRepository<BigQuestion, Long> {

    List<BigQuestion> findByIdIn(Set<Long> bigQuestionIds);

    List<BigQuestion> findByName(String name);

    List<BigQuestion> findByNameAndCreatedDate(String name);
}
