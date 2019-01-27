package com.example.nationalServerRepository;

import com.example.entityServer.DoctorContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorContactRepository extends JpaRepository<DoctorContact,Long>{
}
