package com.example.nationalServerRepository;

import com.example.entityServer.DoctorServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorServerRepository extends JpaRepository<DoctorServer,Long> {
    @Query(value  ="SELECT max(id) from doctor",nativeQuery = true)
    Long findMaxId();
}
