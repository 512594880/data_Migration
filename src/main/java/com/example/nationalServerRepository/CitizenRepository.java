package com.example.nationalServerRepository;

import com.example.entityServer.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CitizenRepository extends JpaRepository<Citizen,Long>{
    @Query(value  ="SELECT max(id) from citizen",nativeQuery = true)
    Long findMaxId();
}
