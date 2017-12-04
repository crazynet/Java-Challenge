package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.AccountNotExistException;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.OverDraftException;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

	private final Map<String, Account> accounts = new ConcurrentHashMap<>();

	@Override
	public void createAccount(Account account) throws DuplicateAccountIdException {
		Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
		if (previousAccount != null) {
			throw new DuplicateAccountIdException("Account id " + account.getAccountId() + " already exists!");
		}
	}

	@Override
	public Account getAccount(String accountId) {
		return accounts.get(accountId);
	}

	@Override
	public void clearAccounts() {
		accounts.clear();
	}

	@Override
	public void transferAmount(String accountFromId, String accountToId, BigDecimal amount) throws RuntimeException {

		checkAccountsExistence(accountFromId, accountToId);
		BigDecimal newAmountFromAccount = checkOverDraftScenario(accountFromId, amount);
		BigDecimal currentAmountToAccount = accounts.get(accountToId).getBalance();
		transferOperation(accountFromId, accountToId, amount, currentAmountToAccount, newAmountFromAccount);

	}

	private BigDecimal checkOverDraftScenario(String accountFromId, BigDecimal amount) {
		BigDecimal currentAmountFromAccount = accounts.get(accountFromId).getBalance();
		BigDecimal newAmountFromAccount = currentAmountFromAccount.subtract(amount);
		if (newAmountFromAccount.signum() == -1) {
			throw new OverDraftException(accountFromId + " does not have enough balance for this transfer to succeed");
		}
		return newAmountFromAccount;
	}

	private void transferOperation(String accountFromId, String accountToId, BigDecimal amount,
			BigDecimal currentAmountToAccount, BigDecimal newAmountFromAccount) {
		BigDecimal newAmountToAccount = currentAmountToAccount.add(amount);
		accounts.get(accountFromId).setBalance(newAmountFromAccount);
		accounts.get(accountToId).setBalance(newAmountToAccount);
	}

	private void checkAccountsExistence(String accountFromId, String accountToId) {
		if (!accounts.containsKey(accountFromId)) {
			throw new AccountNotExistException(accountFromId + " does not exist");
		}
		if (!accounts.containsKey(accountToId)) {
			throw new AccountNotExistException(accountFromId + " does not exist");
		}
	}

}
