package com.example.finance_core.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.finance_core.model.Merchant;

public interface MerchantRepository extends MongoRepository<Merchant, UUID> {
	Optional<Merchant> findByDescription(String description);
}
