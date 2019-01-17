package com.example.newRepository;

import com.example.entity.PageProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PagePropertyRepositoryNew extends JpaRepository<PageProperty,Long> {
    List<PageProperty> findByIdIn(Set<Long> pagePropertiesIds);
}
