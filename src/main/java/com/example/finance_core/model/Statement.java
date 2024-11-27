package com.example.finance_core.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "statement")
public class Statement {
	@Id
	private String id;
	private String transactionId;
	private String merchantCode;
	private Double finalAmount;
	private String status;
	private Date paymentDate;
	private Date generatedAt = new Date();
}