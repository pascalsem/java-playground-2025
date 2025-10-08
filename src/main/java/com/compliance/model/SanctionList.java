package com.compliance.model;

import java.util.HashSet;
import java.util.Set;

public final class SanctionList {

  private final Set<String> sanctionedClientIds;
  private final Set<String> embargoedCountries;

  public SanctionList() {
    this.sanctionedClientIds = new HashSet<>();
    this.embargoedCountries = new HashSet<>();
  }

  public Set<String> getSanctionedClientIds() {
    return sanctionedClientIds;
  }

  public Set<String> getEmbargoedCountries() {
    return embargoedCountries;
  }
}
