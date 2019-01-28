package com.example.newRepository;

import com.example.newEntity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long>{
    List<Patient> findByArea(String areaId);

    Optional<Patient> findByTIdAndId(Long tid,Long patientId);
}
