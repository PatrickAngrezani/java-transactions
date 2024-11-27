package com.example.finance_core.util;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.finance_core.model.Merchant;
import com.example.finance_core.repository.MerchantRepository;

@Service
public class MerchantValidator {

	private final MerchantRepository merchantRepository;

	// Constructor-based injection
	public MerchantValidator(MerchantRepository merchantRepository) {
		this.merchantRepository = merchantRepository;
	}

	// Generate UUID automatically to description if needs
	public String getOrCreateMerchantCode(String description) {
		return merchantRepository.findByDescription(description).map(merchant -> merchant.getMerchantCode().toString())
				.orElseGet(() -> {
					UUID newMerchantCode = UUID.randomUUID();
					merchantRepository.save(new Merchant(newMerchantCode.toString(), description));
					return newMerchantCode.toString();
				});
	}

	public boolean isMerchantValid(String merchantCode, String description) {
		return merchantRepository.findById(merchantCode).map(merchant -> merchant.getDescription().equals(description))
				.orElse(false);
	}
}
