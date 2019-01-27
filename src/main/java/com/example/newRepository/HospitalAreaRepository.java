package com.example.newRepository;

import com.example.newEntity.HospitalArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalAreaRepository extends JpaRepository<HospitalArea,Long> {
    List<HospitalArea> findByHospitalId(Long id);
}
