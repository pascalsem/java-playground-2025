package com.compliance.service;

import static com.compliance.model.ClientType.CORPORATE;
import static com.compliance.model.ClientType.RETAIL;
import static com.compliance.model.ComplianceStatus.APPROVED;
import static com.compliance.model.ComplianceStatus.REJECTED;
import static com.compliance.model.ComplianceStatus.REVIEW_REQUIRED;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;

import com.compliance.dao.ClientDAO;
import com.compliance.dao.SanctionListDAO;
import com.compliance.model.Client;
import com.compliance.model.ClientType;
import com.compliance.model.ComplianceStatus;
import com.compliance.model.Transaction;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

public class ComplianceService {

  private static final BigDecimal EXTREME_FACTOR = new BigDecimal("5");
  private final Map<ClientType, BigDecimal> AMOUNT_THRESHOLDS = ofEntries(
    entry(RETAIL, new BigDecimal("10000")),
    entry(CORPORATE, new BigDecimal("100000"))
  );
  private ClientDAO clientDAO;
  private SanctionListDAO sanctionListDAO;

  public ComplianceStatus evaluate(Transaction tx) {
    try {
      var client = Optional
        .of(tx)
        .map(Transaction::getClientId)
        .map(clientDAO::findById)
        .orElseThrow(ReviewRequiredException::new)
        .orElseThrow(ReviewRequiredException::new);
      return approveOrReject(tx, client);
    } catch (ReviewRequiredException e) {
      return REVIEW_REQUIRED;
    }
  }

  ComplianceStatus approveOrReject(Transaction tx, Client client) {
    var sanctionList = sanctionListDAO.getCurrent();
    // 1. Sanction check
    if (sanctionList.getSanctionedClientIds().contains(client.getId())) {
      return REJECTED;
    }

    // 2. Embargoed destination country
    if (
      tx.getDestinationCountry() != null &&
      sanctionList.getEmbargoedCountries().stream().anyMatch(c -> c.equalsIgnoreCase(tx.getDestinationCountry()))
    ) {
      return REJECTED;
    }

    // 3. Amount threshold dynamic by client type
    var threshold = AMOUNT_THRESHOLDS.getOrDefault(client.getType(), new BigDecimal("5000"));
    if (tx.getAmount() == null) {
      return REVIEW_REQUIRED;
    }
    var amount = tx.getAmount();
    if (amount.compareTo(threshold.multiply(EXTREME_FACTOR)) > 0) {
      return REJECTED;
    }
    if (amount.compareTo(threshold) > 0) {
      return REVIEW_REQUIRED;
    }

    // 4. Gray list heuristic
    if (client.isGrayListed()) {
      return REVIEW_REQUIRED;
    }
    return APPROVED;
  }

  public void setClientDAO(ClientDAO clientDAO) {
    this.clientDAO = clientDAO;
  }

  public void setSanctionListDAO(SanctionListDAO sanctionListDAO) {
    this.sanctionListDAO = sanctionListDAO;
  }
}
