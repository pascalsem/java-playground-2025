package com.compliance;

import com.compliance.dao.ClientDAO;
import com.compliance.dao.SanctionListDAO;
import com.compliance.model.Transaction;
import com.compliance.service.ComplianceService;
import java.math.BigDecimal;
import java.util.UUID;

public class Main {

  public static void main(String[] args) {
    ComplianceService service = new ComplianceService();
    service.setClientDAO(new ClientDAO());
    service.setSanctionListDAO(new SanctionListDAO());

    // Aria is a clean client, payment to Caelithra is authorized, amount is below threshold
    var tx1 = new Transaction();
    tx1.setId(UUID.randomUUID().toString());
    tx1.setClientId("12");
    tx1.setAmount(new BigDecimal("2500"));
    tx1.setCurrency("AZE");
    tx1.setDestinationCountry("Caelithra");
    printResult(service, tx1);

    // ...But payments to Eldwyne are not allowed
    var tx2 = new Transaction();
    tx2.setId(UUID.randomUUID().toString());
    tx2.setClientId("12");
    tx2.setAmount(new BigDecimal("2500"));
    tx2.setCurrency("IOP");
    tx2.setDestinationCountry("Eldwyne");
    printResult(service, tx2);

    // Gandalf is a grayed client and needs some attention
    var tx3 = new Transaction();
    tx3.setId(UUID.randomUUID().toString());
    tx3.setClientId("15");
    tx3.setAmount(new BigDecimal("100"));
    tx3.setCurrency("IOP");
    tx3.setDestinationCountry("Caelithra");
    printResult(service, tx3);
  }

  private static void printResult(ComplianceService service, Transaction tx) {
    System.out.printf("Transaction %s => %s%n", tx.getId(), service.evaluate(tx));
  }
}
