package com.compliance.dao;

import com.compliance.model.SanctionList;
import java.util.Set;

public class SanctionListDAO {

  public SanctionList getCurrent() {
    var sanctionList = new SanctionList();
    var sanctionedRetailClientIds = Set.of("10", "27", "25", "11", "18", "21");
    var sanctionedCorporateClientIds = Set.of("30", "47", "45", "31", "38", "35");
    sanctionList.getSanctionedClientIds().addAll(sanctionedRetailClientIds);
    sanctionList.getSanctionedClientIds().addAll(sanctionedCorporateClientIds);
    var embargoedCountries = Set.of("Nimbrosia", "Eldwyne", "Virelune", "Drakmor", "Sylvaris");
    sanctionList.getEmbargoedCountries().addAll(embargoedCountries);
    return sanctionList;
  }
}
