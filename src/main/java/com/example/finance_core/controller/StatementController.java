package com.example.finance_core.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.finance_core.model.Statement;
import com.example.finance_core.service.StatementService;

@RestController
@RequestMapping("/api/statements")
public class StatementController {
	private final StatementService statementService;

	public StatementController(StatementService statementService) {
		this.statementService = statementService;
	}

	@PostMapping
	public ResponseEntity<Statement> generateStatement(@RequestBody String merchantCode) {
		return ResponseEntity.ok(statementService.generateStatement(merchantCode));
	}
}
