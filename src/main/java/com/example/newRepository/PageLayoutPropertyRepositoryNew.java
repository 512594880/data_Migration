package com.example.newRepository;

import com.example.newEntity.PageLayoutPropertyNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageLayoutPropertyRepositoryNew extends JpaRepository<PageLayoutPropertyNew,Long> {
    List<PageLayoutPropertyNew> findByPageLayoutId(Long id);
}
