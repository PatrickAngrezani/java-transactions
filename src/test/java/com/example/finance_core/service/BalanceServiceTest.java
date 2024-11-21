package com.example.finance_core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.finance_core.model.Transaction;
import com.example.finance_core.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
public class BalanceServiceTest {
	@Mock
	private TransactionRepository transactionRepository;

	@InjectMocks
	private BalanceService balanceService;

	@Test
	void shouldReturnCorrectBalances() {
		// Mocks paid transactions
		List<Transaction> paidTransactions = List.of(new Transaction("merchant1", 100.0, "paid"),
				new Transaction("merchant1", 50.0, "paid"));

		// Mocks of waiting transactions
		List<Transaction> waitingFundsTransactions = List.of(new Transaction("merchant1", 200.0, "waiting_funds"));

		// Set mock behavior
		when(transactionRepository.findByMerchantCodeAndStatus("merchant1", "paid")).thenReturn(paidTransactions);
		when(transactionRepository.findByMerchantCodeAndStatus("merchant1", "waiting_funds"))
				.thenReturn(waitingFundsTransactions);

		// Execute method
		Map<String, Double> balances = balanceService.getBalances("merchant1");

		// Verify results
		assertEquals(150.0, balances.get("available"));
		assertEquals(200, balances.get("waiting_funds"));
	}
}
