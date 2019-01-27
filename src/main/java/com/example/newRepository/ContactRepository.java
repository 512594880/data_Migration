package com.example.newRepository;

import com.example.newEntity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Long>{
    @Query(value = "SELECT * from contact WHERE user_id IN (SELECT id from doctor d WHERE d.t_id = 3)",nativeQuery = true)
    List<Contact> findByUserId();
}
