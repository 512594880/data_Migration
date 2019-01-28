package com.example.newRepository;

import com.example.newEntity.PatientLabelDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientLabelDetailRepository extends JpaRepository<PatientLabelDetail,Long>{
    PatientLabelDetail findByPatientIdAndTaskLabelId(Long id, Long id1);

    List<PatientLabelDetail> findByPatientId(Long patientId);
}
