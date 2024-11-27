package com.example.finance_core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import com.example.finance_core.model.Transaction;
import com.example.finance_core.repository.TransactionRepository;
import com.example.finance_core.util.MerchantValidator;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private MerchantValidator merchantValidator;

	@Mock
	private MongoTemplate mongoTemplate;

	@InjectMocks
	private TransactionService transactionService;

	@Test
	void shouldSaveTransactionSuccesfully() {
		final String merchantCode = "123e4567-e89b-12d3-a456-426614174000";
		final String description = "description1";
		final String cardNumber = "1234567890123456";

		// merchantValidator mock
		when(merchantValidator.getOrCreateMerchantCode(description)).thenReturn(merchantCode);
		when(merchantValidator.isMerchantValid(merchantCode, description)).thenReturn(true);

		// Creating transaction
		Transaction transaction = new Transaction(merchantCode, 95.0, description);
		transaction.setDescription(description);
		transaction.setAmount(100.0);
		transaction.setPaymentMethod("credit");
		transaction.setCardNumber(cardNumber);

		// Mock repository
		when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// Execute method
		Transaction savedTransaction = transactionService.saveTransaction(transaction);

		// Verify changes in object
		assertEquals(merchantCode, savedTransaction.getMerchantCode());
		assertEquals("credit", savedTransaction.getPaymentMethod());
		assertEquals("waiting_funds", savedTransaction.getStatus());
		assertEquals(95.0, savedTransaction.getFinalAmount());
		assertEquals("**** **** **** 3456", savedTransaction.getCardNumber());
	}

	@Test
	void shouldReturnTransactionById() {
		// Transaction mock
		Transaction transaction = new Transaction("merchant1", 97.0, "paid");
		when(transactionRepository.findById("transactionId1")).thenReturn(Optional.of(transaction));

		// Call method
		Optional<Transaction> result = transactionService.getTransactionById("transactionId1");

		// Verify result
		assertTrue(result.isPresent());
		assertEquals("merchant1", result.get().getMerchantCode());
		assertEquals(97.0, result.get().getFinalAmount());
		assertEquals("paid", result.get().getStatus());
	}

	@Test
	void shouldReturnEmptyWhenTransactionNotFound() {
		// Set mock to return empty
		when(transactionRepository.findById("transactionId2")).thenReturn(Optional.empty());

		// Execute method
		Optional<Transaction> result = transactionService.getTransactionById("transactionId2");

		// Verify result is empty
		assertFalse(result.isPresent());
	}

	@Test
	void shouldReturnFilteredTransactions() {
		// Transactions mock
		List<Transaction> transactions = List.of(new Transaction("merchant1", 97.0, "paid"),
				new Transaction("merchant2", 190.0, "waiting_funds"));

		// Set mock behavior
		when(mongoTemplate.find(any(Query.class), eq(Transaction.class))).thenReturn(transactions);

		// Execute method
		List<Transaction> result = transactionService.getFilteredTransactions("Test Description", "credit", "John Doe");

		// Verify results
		assertEquals(2, result.size());
		assertEquals("merchant1", result.get(0).getMerchantCode());
		assertEquals(97.0, result.get(0).getFinalAmount());
		assertEquals("paid", result.get(0).getStatus());
	}

	@Test
	void shouldReturnEmptyWhenNoTransactionsMatch() {
		// Set mock to return empty list
		when(mongoTemplate.find(any(Query.class), any(Class.class))).thenReturn(List.of());

		// Execute method
		List<Transaction> result = transactionService.getFilteredTransactions("No Match", null, null);

		// Verify result is empty
		assertTrue(result.isEmpty());
	}
}
