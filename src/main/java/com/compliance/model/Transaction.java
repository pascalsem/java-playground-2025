package com.compliance.model;

import java.math.BigDecimal;

public class Transaction {

  private String id;
  private String clientId;
  private BigDecimal amount;
  private String currency;
  private String destinationCountry;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getDestinationCountry() {
    return destinationCountry;
  }

  public void setDestinationCountry(String destinationCountry) {
    this.destinationCountry = destinationCountry;
  }
}
