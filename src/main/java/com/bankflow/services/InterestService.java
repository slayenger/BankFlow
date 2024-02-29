package com.bankflow.services;

import com.bankflow.entities.BankAccount;
import com.bankflow.entities.Interest;
import com.bankflow.repositories.BankAccountRepository;
import com.bankflow.repositories.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class InterestService {

    private final InterestRepository interestRepository;
    private final BankAccountRepository bankAccountRepository;


    @Scheduled(initialDelay = 60000,fixedRate = 60000)
    @Async
    public void increaseBalances() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();

        for (BankAccount bankAccount : bankAccounts) {
            BigDecimal currentBalance = bankAccount.getBalance();
            Interest interest = updateInterestTimestamp(bankAccount);
            BigDecimal initialBalance = interest.getInitialBalance();
            BigDecimal increasedBalance = calculateIncreasedBalance(initialBalance, currentBalance);

            if (exceedsBalanceLimit(initialBalance, increasedBalance)) {
                continue;
            }

            updateAccountBalanceAndTimestamp(bankAccount, increasedBalance);
        }
    }

    private Interest updateInterestTimestamp(BankAccount bankAccount) {
        Interest interest = interestRepository.findByBankAccount(bankAccount);
        interest.setTimestamp(new Date());
        return interestRepository.save(interest);
    }

    private void updateAccountBalanceAndTimestamp(BankAccount bankAccount, BigDecimal increasedBalance) {
        bankAccount.setBalance(increasedBalance);
        bankAccount.setUpdatedAt(new Date());
        bankAccountRepository.save(bankAccount);
    }

    private BigDecimal calculateIncreasedBalance(BigDecimal initialBalance, BigDecimal currentBalance) {
        BigDecimal increaseCoefficient = BigDecimal.valueOf(0.05); // 5%
        BigDecimal increasedAmount = initialBalance.multiply(increaseCoefficient);
        return currentBalance.add(increasedAmount);
    }

    private boolean exceedsBalanceLimit(BigDecimal initialBalance, BigDecimal increasedBalance) {
        BigDecimal maxBalanceLimit = initialBalance.multiply(BigDecimal.valueOf(2.07)); // 207%
        return increasedBalance.compareTo(maxBalanceLimit) > 0;
    }


}
