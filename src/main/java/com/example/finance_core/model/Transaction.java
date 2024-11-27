package com.example.finance_core.model;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "transactions")
public class Transaction {
	@Id
	private String id = UUID.randomUUID().toString();
	private String merchantCode = UUID.randomUUID().toString();
	private Double amount;
	private String description;
	private String paymentMethod;
	private Double finalAmount;
	private String status;
	private String cardNumber;
	private String cardHolderName;
	private String cardExpirationDate;
	private String cvv;
	private Date paymentDate;
	private Date createdAt = new Date();

<<<<<<< Updated upstream
	public Double getAmount() {
		return amount;
	}

	public Double getFinalAmount() {
		return finalAmount;
=======
	public Transaction(String merchantCode, Double finalAmount, String status) {
		this.merchantCode = merchantCode;
		this.finalAmount = finalAmount;
		this.status = status;
>>>>>>> Stashed changes
	}
}
