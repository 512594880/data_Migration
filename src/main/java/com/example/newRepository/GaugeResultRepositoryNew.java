package com.example.newRepository;

import com.example.newEntity.GaugeResultNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GaugeResultRepositoryNew extends JpaRepository<GaugeResultNew,Long> {
    GaugeResultNew findByTemplateIdAndResultName(Long id, String name);

    @Query(value="SELECT max(id) from gauge_result", nativeQuery = true)
    public Long findMaxId();
}
