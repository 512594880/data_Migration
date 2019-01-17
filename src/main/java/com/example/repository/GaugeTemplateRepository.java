package com.example.repository;

import com.example.entity.GaugeTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GaugeTemplateRepository extends JpaRepository<GaugeTemplate,Long>{
    List<GaugeTemplate> findByCreatedDateBetweenAndDelFlag(String beginDate, String EndDate, int i);

    boolean findByTemplateName(String templateName);
}
