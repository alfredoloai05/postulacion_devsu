package com.devsu.hackerearth.backend.account.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.PartialAccountDto;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;

import java.util.stream.Collectors;
import com.devsu.hackerearth.backend.account.model.Account;

@Service
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;

	public AccountServiceImpl(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

    @Override
    public List<AccountDto> getAll() {
        return accountRepository.findAll()
            .stream()
            .map(account -> new AccountDto(
                account.getId(),
                account.getNumber(),
                account.getType(),
                account.getInitialAmount(),
                account.isActive(),
                account.getClientId()
            ))
            .collect(Collectors.toList());
    }

    @Override
    public AccountDto getById(Long id) {
        return accountRepository.findById(id)
            .map(account -> new  AccountDto(
                account.getId(),
                account.getNumber(),
                account.getType(),
                account.getInitialAmount(),
                account.isActive(),
                account.getClientId()               
            ))
            .orElse(null);
    }

    @Override
    public AccountDto create(AccountDto accountDto) {
        Account account = new Account();

        account.setNumber(accountDto.getNumber());
        account.setType(accountDto.getType());
        account.setInitialAmount(accountDto.getInitialAmount());
        account.setActive(accountDto.isActive());
        account.setClientId(accountDto.getClientId());

        Account savedAccount = accountRepository.save(account);

        return new AccountDto(
            savedAccount.getId(),
            savedAccount.getNumber(),
            savedAccount.getType(),
            savedAccount.getInitialAmount(),
            savedAccount.isActive(),
            savedAccount.getClientId()
        );
    }

    @Override
    public AccountDto update(AccountDto accountDto) {

        Account account = accountRepository.findById(accountDto.getId())
            .orElse(null);

        if (account == null){
            return null;
        }    

        account.setNumber(accountDto.getNumber());
        account.setType(accountDto.getType());
        account.setInitialAmount(accountDto.getInitialAmount());
        account.setActive(accountDto.isActive());
        account.setClientId(accountDto.getClientId());

        Account savedAccount = accountRepository.save(account);

        return new AccountDto(
            savedAccount.getId(),
            savedAccount.getNumber(),
            savedAccount.getType(),
            savedAccount.getInitialAmount(),
            savedAccount.isActive(),
            savedAccount.getClientId()
        );
    }

    @Override
    public AccountDto partialUpdate(Long id, PartialAccountDto partialAccountDto) {
        Account account = accountRepository.findById(id)
        .orElse(null);

        if (account == null){
            return null;
        }    

        account.setActive(partialAccountDto.isActive());

        Account savedAccount = accountRepository.save(account);

        return new AccountDto(
            savedAccount.getId(),
            savedAccount.getNumber(),
            savedAccount.getType(),
            savedAccount.getInitialAmount(),
            savedAccount.isActive(),
            savedAccount.getClientId()
        );
    }

    @Override
    public void deleteById(Long id) {
		if (accountRepository.existsById(id)){
			accountRepository.deleteById(id);
		}
    }
}
