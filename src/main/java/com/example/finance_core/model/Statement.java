package com.example.finance_core.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public void setTotalAmount(Double finalAmount) {
		this.finalAmount = finalAmount;
	}
}