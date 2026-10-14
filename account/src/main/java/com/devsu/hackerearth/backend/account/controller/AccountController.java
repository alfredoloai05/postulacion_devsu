package com.devsu.hackerearth.backend.account.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.PartialAccountDto;
import com.devsu.hackerearth.backend.account.service.AccountService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@GetMapping
	public ResponseEntity<List<AccountDto>> getAll() {
		return ResponseEntity.ok(accountService.getAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<AccountDto> get(@PathVariable Long id) {
		AccountDto account = accountService.getById(id);

		if (account == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(account);
	}

	@PostMapping
	public ResponseEntity<AccountDto> create(@RequestBody AccountDto accountDto) {
		AccountDto createAccount = accountService.create(accountDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(createAccount);
	}

	@PutMapping("/{id}")
	public ResponseEntity<AccountDto> update(@PathVariable Long id, @RequestBody AccountDto accountDto) {
		accountDto.setId(id);

		AccountDto updateAccount = accountService.update(accountDto);

		if (updateAccount == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(updateAccount);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<AccountDto> partialUpdate(@PathVariable Long id,
			@RequestBody PartialAccountDto partialAccountDto) {
		AccountDto updateAccount = accountService.partialUpdate(id, partialAccountDto);

		if (updateAccount == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(updateAccount);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		AccountDto account = accountService.getById(id);

		if (account == null) {
			return ResponseEntity.notFound().build();
		}

		accountService.deleteById(id);

		return ResponseEntity.noContent().build();
	}
}
