package com.example.newRepository;

import com.example.newEntity.TaskPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskPatientRepository extends JpaRepository<TaskPatient,Long>{
    List<TaskPatient> findByTaskId(Long id);
}
