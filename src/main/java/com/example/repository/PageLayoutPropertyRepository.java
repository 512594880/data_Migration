package com.example.repository;

import com.example.entity.PageLayoutProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageLayoutPropertyRepository extends JpaRepository<PageLayoutProperty,Long> {
}
