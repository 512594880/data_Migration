package com.example.newRepository;

import com.example.entity.PageLayout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageLayoutRepositoryNew extends JpaRepository<PageLayout,Long> {
    List<PageLayout> findByGaugeTemplateId(Long id);
}
