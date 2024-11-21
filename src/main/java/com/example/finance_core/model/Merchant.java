package com.example.finance_core.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "merchants")
public class Merchant {
	@Id
	private UUID merchantCode;
	private String description;

	public Merchant() {
	}

	public Merchant(UUID merchantCode, String description) {
		this.merchantCode = merchantCode;
		this.description = description;
	}

	public UUID getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(UUID merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
