package com.example.newRepository;

import com.example.newEntity.PatientDimension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientDimensionRepository extends JpaRepository<PatientDimension,Long>{
}
