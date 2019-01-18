package com.example.newRepository;

import com.example.newEntity.PagePropertyNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PagePropertyRepositoryNew extends JpaRepository<PagePropertyNew,Long> {
    List<PagePropertyNew> findByIdIn(Set<Long> pagePropertiesIds);
}
