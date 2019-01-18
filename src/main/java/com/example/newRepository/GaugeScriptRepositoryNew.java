package com.example.newRepository;

import com.example.newEntity.GaugeScriptNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GaugeScriptRepositoryNew extends JpaRepository<GaugeScriptNew,Long> {
    List<GaugeScriptNew> findByScriptName(String scriptName);
    @Query(value ="SELECT max(id) from gauge_script", nativeQuery = true)
    Long findMaxId();
}
