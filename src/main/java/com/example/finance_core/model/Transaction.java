package com.example.finance_core.model;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

	public Double getAmount() {
		return amount;
	}

	public Double getFinalAmount() {
		return finalAmount;
	}
}
