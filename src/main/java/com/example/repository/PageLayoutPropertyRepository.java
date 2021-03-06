package com.example.repository;

import com.example.entity.PageLayoutProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageLayoutPropertyRepository extends JpaRepository<PageLayoutProperty,Long> {
    List<PageLayoutProperty> findByPageLayoutId(Long id);
}
