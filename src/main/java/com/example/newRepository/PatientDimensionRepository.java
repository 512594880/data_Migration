package com.example.newRepository;

import com.example.newEntity.PatientDimension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientDimensionRepository extends JpaRepository<PatientDimension,Long>{
    List<PatientDimension> findByPatientId(Long id);
}
