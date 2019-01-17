package com.example.repository;

import com.example.entity.GaugeResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GaugeResultRepository extends JpaRepository<GaugeResult,Long> {
    List<GaugeResult> findByTemplateId(Long id);
}
