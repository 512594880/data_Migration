package com.example.newRepository;

import com.example.entity.GaugeTemplateScript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GaugeTemplateScriptRepositoryNew extends JpaRepository<GaugeTemplateScript,Long> {
    List<GaugeTemplateScript> findByTemplateId(Long id);

    List<GaugeTemplateScript> findByTemplateIdAndScriptId(Long templateId, Long scriptId);
}
