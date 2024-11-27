package com.example.finance_core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.finance_core.model.Statement;
import com.example.finance_core.model.Transaction;
import com.example.finance_core.repository.StatementRepository;
import com.example.finance_core.repository.TransactionRepository;

@Service
public class StatementService {
	private final StatementRepository statementRepository;
	private final TransactionRepository transactionRepository;

	public StatementService(StatementRepository statementRepository, TransactionRepository transactionRepository) {
		this.statementRepository = statementRepository;
		this.transactionRepository = transactionRepository;
	}

	public Statement generateStatement(String merchantCode) {
		List<Transaction> transactions = transactionRepository.findByMerchantCode(merchantCode);
		Double finalAmount = transactions.stream().mapToDouble(Transaction::getFinalAmount).sum();

		Statement statement = new Statement();
		statement.setMerchantCode(merchantCode);
		statement.setFinalAmount(finalAmount);

		return statementRepository.save(statement);
	}
}
