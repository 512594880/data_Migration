package com.example.repository;

import com.example.entity.GaugeScript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GaugeScriptRepository extends JpaRepository<GaugeScript,Long> {
}
