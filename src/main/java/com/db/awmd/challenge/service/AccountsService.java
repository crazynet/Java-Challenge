package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.repository.AccountsRepository;
import lombok.Getter;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountsService {

  @Getter
  private final AccountsRepository accountsRepository;
  
  @Getter
  private final NotificationService notificationService;

  @Autowired
  public AccountsService(AccountsRepository accountsRepository,NotificationService notificationService) {
    this.accountsRepository = accountsRepository;
    this.notificationService=notificationService;
  }

  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }

  public Account getAccount(String accountId) {
    return this.accountsRepository.getAccount(accountId);
  }
  public void transferAmount(String accountFromId,String accountToId,BigDecimal amount) {
	    this.accountsRepository.transferAmount(accountFromId,accountToId,amount);
	    notificationService.notifyAboutTransfer(new Account(accountToId), amount.toString());
	    notificationService.notifyAboutTransfer(new Account(accountFromId), amount.toString());
	  }
}
