package com.example.newRepository;

import com.example.newEntity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,Long> {
    @Query(value  ="SELECT * from doctor WHERE t_id = 3",nativeQuery = true)
    List<Doctor> findByTId();
}
