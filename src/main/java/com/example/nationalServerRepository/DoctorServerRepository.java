package com.example.nationalServerRepository;

import com.example.entityServer.DoctorServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorServerRepository extends JpaRepository<DoctorServer,Long> {
}
