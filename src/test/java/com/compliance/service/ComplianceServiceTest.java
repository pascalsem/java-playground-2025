package com.compliance.service;

import static com.compliance.model.ClientType.CORPORATE;
import static com.compliance.model.ClientType.RETAIL;
import static com.compliance.model.ComplianceStatus.APPROVED;
import static com.compliance.model.ComplianceStatus.REJECTED;
import static com.compliance.model.ComplianceStatus.REVIEW_REQUIRED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.compliance.dao.ClientDAO;
import com.compliance.dao.SanctionListDAO;
import com.compliance.model.Client;
import com.compliance.model.SanctionList;
import com.compliance.model.Transaction;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ComplianceServiceTest {

  @Mock
  ClientDAO clientDAO;

  @Mock
  SanctionListDAO sanctionListDAO;

  @InjectMocks
  ComplianceService service;

  private Client retailClient;
  private Client corporateClient;

  @BeforeEach
  void setUp() {
    retailClient = client("10", RETAIL, false);
    corporateClient = client("30", CORPORATE, false);
  }

  private Client client(String id, com.compliance.model.ClientType type, boolean gray) {
    var c = new Client();
    c.setId(id);
    c.setType(type);
    c.setGrayListed(gray);
    c.setName("Test" + id);
    return c;
  }

  private Transaction tx(String clientId, BigDecimal amount) {
    var t = new Transaction();
    t.setClientId(clientId);
    t.setAmount(amount);
    t.setCurrency("USD");
    return t;
  }

  private SanctionList emptySanctions() {
    return new SanctionList();
  }

  private SanctionList withSanctionedClient(String... ids) {
    var list = new SanctionList();
    for (String id : ids) {
      list.getSanctionedClientIds().add(id);
    }
    return list;
  }

  private SanctionList withEmbargoedCountry(String... countries) {
    var list = new SanctionList();
    for (String c : countries) {
      list.getEmbargoedCountries().add(c);
    }
    return list;
  }

  @Test
  void evaluateWithClientNotFound() {
    var tx = tx("missing", new BigDecimal("100"));
    when(clientDAO.findById("missing")).thenReturn(Optional.empty());
    assertEquals(REVIEW_REQUIRED, service.evaluate(tx));
  }

  @Test
  void evaluateWithSanctionedClient() {
    when(clientDAO.findById(retailClient.getId())).thenReturn(Optional.of(retailClient));
    when(sanctionListDAO.getCurrent()).thenReturn(withSanctionedClient(retailClient.getId()));
    var tx = tx(retailClient.getId(), new BigDecimal("100"));
    assertEquals(REJECTED, service.evaluate(tx));
  }

  @Test
  void evaluateWithEmbargoedCountry() {
    when(clientDAO.findById(retailClient.getId())).thenReturn(Optional.of(retailClient));
    var sanctions = withEmbargoedCountry("NIMBROSIA");
    when(sanctionListDAO.getCurrent()).thenReturn(sanctions);
    var tx = tx(retailClient.getId(), new BigDecimal("100"));
    tx.setDestinationCountry("nimbrosia"); // lower case to test case-insensitive match
    assertEquals(REJECTED, service.evaluate(tx));
  }

  @Test
  void evaluateWithoutAmount() {
    when(clientDAO.findById(retailClient.getId())).thenReturn(Optional.of(retailClient));
    when(sanctionListDAO.getCurrent()).thenReturn(emptySanctions());
    var tx = tx(retailClient.getId(), null);
    assertEquals(REVIEW_REQUIRED, service.evaluate(tx));
  }

  @Test
  void evaluateWithExtremeAmount() {
    when(clientDAO.findById(retailClient.getId())).thenReturn(Optional.of(retailClient));
    when(sanctionListDAO.getCurrent()).thenReturn(emptySanctions());
    // Retail threshold = 10000, extreme factor = 5 => 50000; use 50000.01
    var tx = tx(retailClient.getId(), new BigDecimal("50000.01"));
    assertEquals(REJECTED, service.evaluate(tx));
  }

  @Test
  void evaluateWithAmountAboveThreshold() {
    when(clientDAO.findById(retailClient.getId())).thenReturn(Optional.of(retailClient));
    when(sanctionListDAO.getCurrent()).thenReturn(emptySanctions());
    // Above 10000 but below 50000
    var tx = tx(retailClient.getId(), new BigDecimal("15000"));
    assertEquals(REVIEW_REQUIRED, service.evaluate(tx));
  }

  @Test
  void evaluateWithGrayListed() {
    var gray = client("15", RETAIL, true);
    when(clientDAO.findById(gray.getId())).thenReturn(Optional.of(gray));
    when(sanctionListDAO.getCurrent()).thenReturn(emptySanctions());
    var tx = tx(gray.getId(), new BigDecimal("500"));
    assertEquals(REVIEW_REQUIRED, service.evaluate(tx));
  }

  @Test
  void evaluateWithRetailClient() {
    when(clientDAO.findById(retailClient.getId())).thenReturn(Optional.of(retailClient));
    when(sanctionListDAO.getCurrent()).thenReturn(emptySanctions());
    var tx = tx(retailClient.getId(), new BigDecimal("10000"));
    assertEquals(APPROVED, service.evaluate(tx));
  }

  @Test
  void evaluateWithCorporate() {
    when(clientDAO.findById(corporateClient.getId())).thenReturn(Optional.of(corporateClient));
    when(sanctionListDAO.getCurrent()).thenReturn(emptySanctions());
    var tx = tx(corporateClient.getId(), new BigDecimal("100000"));
    assertEquals(APPROVED, service.evaluate(tx));
  }
}
