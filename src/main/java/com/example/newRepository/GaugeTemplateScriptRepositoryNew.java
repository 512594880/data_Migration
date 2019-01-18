package com.example.newRepository;

import com.example.newEntity.GaugeTemplateScriptNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GaugeTemplateScriptRepositoryNew extends JpaRepository<GaugeTemplateScriptNew,Long> {
    List<GaugeTemplateScriptNew> findByTemplateId(Long id);

    List<GaugeTemplateScriptNew> findByTemplateIdAndScriptId(Long templateId, Long scriptId);

    @Query(value ="SELECT max(id) from gauge_template_script", nativeQuery = true)
    Long findMaxId();
}
