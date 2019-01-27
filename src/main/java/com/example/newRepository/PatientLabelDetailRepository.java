package com.example.newRepository;

import com.example.newEntity.PatientLabelDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientLabelDetailRepository extends JpaRepository<PatientLabelDetail,Long>{
    PatientLabelDetail findByPatientIdAndTaskLabelId(Long id, Long id1);
}
