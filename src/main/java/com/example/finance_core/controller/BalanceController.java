package com.example.finance_core.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.finance_core.service.BalanceService;

@RestController
@RequestMapping("api/balances")
public class BalanceController {
	private final BalanceService balanceService;

	public BalanceController(BalanceService balanceService) {
		this.balanceService = balanceService;
	}

	@GetMapping("/{merchantCode}")
	public ResponseEntity<Map<String, Double>> getBalances(@PathVariable String merchantCode) {
		Map<String, Double> balances = balanceService.getBalances(merchantCode);
		return ResponseEntity.ok(balances);
	}
}
