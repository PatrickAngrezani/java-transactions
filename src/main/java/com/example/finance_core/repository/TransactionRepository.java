package com.example.finance_core.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.finance_core.model.Transaction;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
	List<Transaction> findByMerchantCode(String merchantCode);

	List<Transaction> findByMerchantCodeAndStatus(String merchantCode, String status);

}
