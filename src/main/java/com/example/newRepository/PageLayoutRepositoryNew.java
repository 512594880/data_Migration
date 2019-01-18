package com.example.newRepository;

import com.example.newEntity.PageLayoutNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageLayoutRepositoryNew extends JpaRepository<PageLayoutNew,Long> {
    List<PageLayoutNew> findByGaugeTemplateId(Long id);
}
