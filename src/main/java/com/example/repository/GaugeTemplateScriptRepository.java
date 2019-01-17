package com.example.repository;

import com.example.entity.GaugeTemplateScript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GaugeTemplateScriptRepository extends JpaRepository<GaugeTemplateScript,Long> {
}
