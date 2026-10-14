package com.devsu.hackerearth.backend.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.devsu.hackerearth.backend.account.controller.AccountController;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;
import com.devsu.hackerearth.backend.account.service.AccountServiceImpl;


@SpringBootTest
public class sampleTest {

	@Autowired
	private AccountServiceImpl realAccountService;

	@Autowired
	private AccountRepository accountRepository;

	private AccountService accountService = mock(AccountService.class);
	private AccountController accountController = new AccountController(accountService);

	@Test
	void createAccountTest() {
		// Arrange
		AccountDto newAccount = new AccountDto(1L, "number", "savings", 0.0, true, 1L);
		AccountDto createdAccount = new AccountDto(1L, "number", "savings", 0.0, true, 1L);
		when(accountService.create(newAccount)).thenReturn(createdAccount);

		// Act
		ResponseEntity<AccountDto> response = accountController.create(newAccount);

		// Assert
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(createdAccount, response.getBody());
	}

	@Test
	@Transactional
	void integrationCreateAccountTest() {

		AccountDto input = new AccountDto(
			null,"F6-TEST","Ahorros",1000.0,true,1L
		);

		AccountDto saved = realAccountService.create(input);

		assertNotNull(saved.getId());

		assertTrue(
			accountRepository.existsById(saved.getId())
		);
	}
}

