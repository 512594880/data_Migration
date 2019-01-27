package com.example.newRepository;

import com.example.newEntity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalRepository  extends JpaRepository<Hospital,Long>{
    Hospital findByHospitalName(String hospitalName);

    List<Hospital> findByParentId(Long parentId);
}
