package com.db.awmd.challenge.domain;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AccountsTransfer {

  @NotNull
  @NotEmpty
  private final String accountFromId;

  @NotNull
  @NotEmpty
  private final String accountToId;
  
  @NotNull
  @Min(value = 1, message = "Amount must be positive.")
  private BigDecimal amount;

  @JsonCreator
  public AccountsTransfer(@JsonProperty("accountFromId") String accountFromId,@JsonProperty("accountToId") String accountToId,
    @JsonProperty("amount") BigDecimal amount) {
	  this.accountFromId=accountFromId;
	    this.accountToId=accountToId;
	    this.amount=amount;
  }
}
