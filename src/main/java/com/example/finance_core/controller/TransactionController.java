package com.example.finance_core.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.finance_core.model.Transaction;
import com.example.finance_core.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
	private final TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@PostMapping
	public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
		return ResponseEntity.ok(transactionService.saveTransaction(transaction));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Transaction> getTransaction(@PathVariable String id) {
		return transactionService.getTransactionById(id).map(ResponseEntity::ok)
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
	}

	@GetMapping("/filter")
	public ResponseEntity<List<Transaction>> filterTransactions(
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "paymentMethod", required = false) String paymentMethod,
			@RequestParam(value = "cardHolderName", required = false) String cardHolderName) {
		List<Transaction> transactions = transactionService.getFilteredTransactions(description, paymentMethod,
				cardHolderName);
		return ResponseEntity.ok(transactions);
	}
}
