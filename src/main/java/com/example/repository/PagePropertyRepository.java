package com.example.repository;

import com.example.entity.PageProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagePropertyRepository extends JpaRepository<PageProperty,Long> {
}
