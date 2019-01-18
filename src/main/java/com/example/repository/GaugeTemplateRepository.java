package com.example.repository;

import com.example.entity.GaugeTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface GaugeTemplateRepository extends JpaRepository<GaugeTemplate,Long>{
    List<GaugeTemplate> findByDelFlagAndCreatedDateBetween(int i,Date beginDate, Date EndDate);

    boolean findByTemplateName(String templateName);

}
