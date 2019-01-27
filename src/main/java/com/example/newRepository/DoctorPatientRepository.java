package com.example.newRepository;

import com.example.newEntity.DoctorPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorPatientRepository extends JpaRepository<DoctorPatient,Long>{
}
