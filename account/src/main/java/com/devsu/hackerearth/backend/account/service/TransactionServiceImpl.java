package com.devsu.hackerearth.backend.account.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.devsu.hackerearth.backend.account.model.dto.BankStatementDto;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;
import com.devsu.hackerearth.backend.account.repository.TransactionRepository;

import java.util.stream.Collectors;
import com.devsu.hackerearth.backend.account.model.Transaction;

import com.devsu.hackerearth.backend.account.repository.AccountRepository;

import org.springframework.transaction.annotation.Transactional;
import com.devsu.hackerearth.backend.account.model.Account;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

import com.devsu.hackerearth.backend.account.repository.ClientSnapshotRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final ClientSnapshotRepository clientSnapshotRepository;

	public TransactionServiceImpl(
        TransactionRepository transactionRepository,
        AccountRepository accountRepository,
        ClientSnapshotRepository clientSnapshotRepository) {

		this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.clientSnapshotRepository = clientSnapshotRepository;
	}

    @Override
    public List<TransactionDto> getAll() {
        return transactionRepository.findAll()
        .stream()
        .map(transaction -> new TransactionDto(
            transaction.getId(),
            transaction.getDate(),
            transaction.getType(),
            transaction.getAmount(),
            transaction.getBalance(),
            transaction.getAccountId()
        ))
        .collect(Collectors.toList());
    }

    @Override
    public TransactionDto getById(Long id) {
        return transactionRepository.findById(id)
        .map(transaction -> new TransactionDto(
            transaction.getId(),
            transaction.getDate(),
            transaction.getType(),
            transaction.getAmount(),
            transaction.getBalance(),
            transaction.getAccountId()
        ))
        .orElse(null);
    }

    @Override
    @Transactional
    public TransactionDto create(TransactionDto transactionDto) {
        com.devsu.hackerearth.backend.account.model.Account account = accountRepository
            .findByIdForUpdate(transactionDto.getAccountId())
            .orElseThrow(() -> new IllegalArgumentException(
                "Cuenta no encontrada"));

        if (!account.isActive()){
            throw new IllegalArgumentException("Cuenta inactiva");
        }

        Transaction last = transactionRepository
            .findTopByAccountIdOrderByIdDesc(account.getId());

        double currentBalance = last == null
            ? account.getInitialAmount()
            : last.getBalance();    

        double amount = transactionDto.getAmount();

        if(!Double.isFinite(amount) || amount == 0 ){
            throw new IllegalArgumentException("Monto invalido");
        }

        double newBalance = currentBalance + amount;

        if(newBalance < 0 ){
            throw new IllegalArgumentException("Saldo no disponible");
        }

        Transaction transaction = new Transaction();

        transaction.setDate(new Date());
        transaction.setType(transactionDto.getType());
        transaction.setAmount(amount);
        transaction.setBalance(newBalance);
        transaction.setAccountId(account.getId());

        Transaction saved = transactionRepository.save(transaction);

        return new TransactionDto(
            saved.getId(),
            saved.getDate(),
            saved.getType(),
            saved.getAmount(),
            saved.getBalance(),
            saved.getAccountId()
        );
    }

    @Override
    public List<BankStatementDto> getAllByAccountClientIdAndDateBetween(Long clientId, Date dateTransactionStart,Date dateTransactionEnd) {
        List<BankStatementDto> report = new ArrayList<>();

        if (dateTransactionStart.after(dateTransactionEnd)){
            throw new IllegalArgumentException("Rango de fechas invalido");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTransactionEnd);
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND,999);

        Date endDate = calendar.getTime();

        List<Account> accounts = accountRepository.findByClientId(clientId);

        String clientName = clientSnapshotRepository.findById(clientId)
            .map(snapshot -> snapshot.getName())
            .orElse(null);

        for (Account account : accounts){

            List <Transaction> transactions = transactionRepository
                .findByAccountIdAndDateBetweenOrderByDateAsc(
                    account.getId(),
                    dateTransactionStart,
                    endDate
                );

            for (Transaction transaction: transactions) {
                
                report.add(new BankStatementDto(
                    transaction.getDate(), 
                    clientName, 
                    account.getNumber(), 
                    account.getType(), 
                    account.getInitialAmount(), 
                    account.isActive(), 
                    transaction.getType(), 
                    transaction.getAmount(), 
                    transaction.getBalance()
                ));
            }
        }

        report.sort(Comparator.comparing(BankStatementDto::getDate));

        return report;

    }

    @Override
    public TransactionDto getLastByAccountId(Long accountId) {
        Transaction transaction = transactionRepository
            .findTopByAccountIdOrderByIdDesc(accountId);

        if(transaction == null){
            return null;
        }

        return new TransactionDto(
            transaction.getId(),
            transaction.getDate(),
            transaction.getType(),
            transaction.getAmount(),
            transaction.getBalance(),
            transaction.getAccountId()
        );
    }
    
}
