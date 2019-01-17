package com.example.repository;

import com.example.entity.PageLayout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PageLayoutRepository extends JpaRepository<PageLayout,Long> {
}
