package com.devsu.hackerearth.backend.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsu.hackerearth.backend.account.model.Transaction;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findTopByAccountIdOrderByIdDesc(Long accountId);

    List<Transaction>findByAccountIdAndDateBetweenOrderByDateAsc(Long accontId, Date start, Date end);

}
