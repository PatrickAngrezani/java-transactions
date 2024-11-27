package com.example.finance_core.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.example.finance_core.model.Transaction;
import com.example.finance_core.repository.TransactionRepository;
import com.example.finance_core.util.MerchantValidator;

@Service
public class TransactionService {
	private final TransactionRepository transactionRepository;
	private final MerchantValidator merchantValidator;

	public TransactionService(TransactionRepository transactionRepository, MerchantValidator merchantValidator) {
		this.transactionRepository = transactionRepository;
		this.merchantValidator = merchantValidator;
	}

	@Autowired
	private MongoTemplate mongoTemplate;

	public Transaction saveTransaction(Transaction transaction) {
		String merchantCode = merchantValidator.getOrCreateMerchantCode(transaction.getDescription());
		transaction.setMerchantCode(merchantCode);

		// Valid if merchantCode and descriptions are linked
//		UUID UuidmerchantCode = UUID.fromString(merchantCode);
		if (!merchantValidator.isMerchantValid(merchantCode, transaction.getDescription())) {
			throw new IllegalArgumentException("Merchant code and description do not match");
		}

		String paymentMethod = transaction.getPaymentMethod();
		transaction.setPaymentMethod(paymentMethod);

		double finalAmount = calculateFinalAmount(transaction.getAmount(), paymentMethod);
		transaction.setFinalAmount(finalAmount);

		String status = paymentMethod.equalsIgnoreCase("debit") ? "paid" : "waiting_funds";
		transaction.setStatus(status);

		Date paymentDate = calculatePaymentDate(paymentMethod);
		transaction.setPaymentDate(paymentDate);

		String cardNumber = transaction.getCardNumber();
		if (cardNumber != null && cardNumber.length() >= 4) {
			String maskedCardNumber = "*** **** **** " + cardNumber.substring(cardNumber.length() - 4);
			transaction.setCardNumber(maskedCardNumber);
		}

		return transactionRepository.save(transaction);
	}

	private double calculateFinalAmount(Double amount, String paymentMethod) {
		double fee = paymentMethod.equalsIgnoreCase("credit") ? 0.05 : 0.03;
		return amount - (amount * fee);
	}

	private Date calculatePaymentDate(String paymentMethod) {
		Calendar calendar = Calendar.getInstance();
		if (paymentMethod.equalsIgnoreCase("credit")) {
			calendar.add(Calendar.DAY_OF_MONTH, 30);
		}

		return calendar.getTime();
	}

	public Optional<Transaction> getTransactionById(String id) {
		return transactionRepository.findById(id);
	}

	public List<Transaction> getFilteredTransactions(String description, String paymentMethod, String cardHolderName) {
		Query query = new Query();
		if (description != null) {
			query.addCriteria(Criteria.where("description").regex("^" + description + "$", "i"));
		}
		if (paymentMethod != null) {
			query.addCriteria(Criteria.where("paymentMethod").regex("^" + paymentMethod + "$", "i"));
		}
		if (cardHolderName != null) {
			query.addCriteria(Criteria.where("cardHolderName").regex("^" + cardHolderName + "$", "i"));
		}

		try {
			return mongoTemplate.find(query, Transaction.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
