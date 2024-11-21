package com.example.finance_core.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.finance_core.model.Transaction;
import com.example.finance_core.repository.TransactionRepository;

@Service
public class BalanceService {
	private final TransactionRepository transactionRepository;

	public BalanceService(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	public Map<String, Double> getBalances(String merchantCode) {
		Double availableBalance = transactionRepository.findByMerchantCodeAndStatus(merchantCode, "paid").stream()
				.mapToDouble(Transaction::getFinalAmount).sum();

		Double WaitingFundsBalance = transactionRepository.findByMerchantCodeAndStatus(merchantCode, "waiting_funds")
				.stream().mapToDouble(Transaction::getFinalAmount).sum();

		System.out.println(availableBalance);
		System.out.println(WaitingFundsBalance);

		Map<String, Double> balances = new HashMap<>();
		balances.put("available", availableBalance);
		balances.put("waiting_funds", WaitingFundsBalance);
		return balances;
	}
}
