package com.example.nationalServerRepository;

import com.example.entityServer.DoctorClinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorClinicRepository extends JpaRepository<DoctorClinic,Long>{
    @Query(value  ="SELECT max(id) from doctor_clinic",nativeQuery = true)
    Long findMaxId();

    DoctorClinic findByName(String hospitalName);
}
