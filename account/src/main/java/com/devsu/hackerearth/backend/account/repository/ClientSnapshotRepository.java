package com.devsu.hackerearth.backend.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsu.hackerearth.backend.account.model.ClientSnapshot;

@Repository
public interface ClientSnapshotRepository extends JpaRepository<ClientSnapshot, Long>{
    
}
