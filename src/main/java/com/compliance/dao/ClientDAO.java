package com.compliance.dao;

import static com.compliance.model.ClientType.CORPORATE;
import static com.compliance.model.ClientType.RETAIL;
import static java.util.Map.entry;
import static java.util.Optional.ofNullable;

import com.compliance.model.Client;
import com.compliance.model.ClientType;
import java.util.Map;
import java.util.Optional;

public class ClientDAO {

  private final Map<String, Client> STORAGE = Map.ofEntries(
    ofClient("10", "Elowen", RETAIL, false),
    ofClient("11", "Thorin", RETAIL, false),
    ofClient("12", "Aria", RETAIL, false),
    ofClient("13", "Frodo", RETAIL, false),
    ofClient("14", "Luthien", RETAIL, false),
    ofClient("15", "Gandalf", RETAIL, true),
    ofClient("16", "Nimue", RETAIL, false),
    ofClient("17", "Alaric", RETAIL, false),
    ofClient("18", "Yvaine", RETAIL, false),
    ofClient("19", "Eldrin", RETAIL, false),
    ofClient("20", "Seraphina", RETAIL, false),
    ofClient("21", "Draven", RETAIL, false),
    ofClient("22", "Lyra", RETAIL, false),
    ofClient("23", "Balin", RETAIL, false),
    ofClient("24", "Morrigan", RETAIL, false),
    ofClient("25", "Faelan", RETAIL, false),
    ofClient("26", "Isolde", RETAIL, false),
    ofClient("27", "Tyrion", RETAIL, false),
    ofClient("28", "Aeliana", RETAIL, false),
    ofClient("29", "Cedric", RETAIL, false),
    ofClient("30", "MysticForge Ltd.", CORPORATE, false),
    ofClient("31", "Dragonspire Inc.", CORPORATE, false),
    ofClient("32", "ElvenTech Corp.", CORPORATE, false),
    ofClient("33", "ArcaneWorks", CORPORATE, true),
    ofClient("34", "CrystalCoven Co.", CORPORATE, false),
    ofClient("35", "Wyrmhold Enterprises", CORPORATE, false),
    ofClient("36", "Moonshade Group", CORPORATE, false),
    ofClient("37", "PhoenixNest Ltd.", CORPORATE, false),
    ofClient("38", "Runestone Dynamics", CORPORATE, false),
    ofClient("39", "ShadowVale Inc.", CORPORATE, false),
    ofClient("40", "CelestialVault", CORPORATE, false),
    ofClient("41", "GriffinGate Holdings", CORPORATE, true),
    ofClient("42", "EnchantedLedger", CORPORATE, false),
    ofClient("43", "MythosBank", CORPORATE, false),
    ofClient("44", "TwilightTrust", CORPORATE, false),
    ofClient("45", "FaerieBloom Ltd.", CORPORATE, false),
    ofClient("46", "Spellbound Systems", CORPORATE, false),
    ofClient("47", "Chimera Capital", CORPORATE, false),
    ofClient("48", "Wandwright Solutions", CORPORATE, true),
    ofClient("49", "Starlight Syndicate", CORPORATE, false)
  );

  Map.Entry<String, Client> ofClient(String id, String name, ClientType type, boolean grayListed) {
    var client = new Client();
    client.setId(id);
    client.setName(name);
    client.setType(type);
    client.setGrayListed(grayListed);
    return entry(id, client);
  }

  public Optional<Client> findById(String id) {
    return ofNullable(STORAGE.get(id));
  }
}
