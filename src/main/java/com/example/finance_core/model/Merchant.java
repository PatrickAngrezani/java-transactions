package com.example.finance_core.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "merchants")
public class Merchant {
	@Id
	private String merchantCode = UUID.randomUUID().toString();
	private String description;

	public Merchant(String merchantCode, String description) {
		this.merchantCode = merchantCode;
		this.description = description;
	}

	public Merchant() {
	}
}
