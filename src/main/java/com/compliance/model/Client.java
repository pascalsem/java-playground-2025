package com.compliance.model;

public class Client {

  private String id;
  private String name;
  private ClientType type;
  private boolean grayListed;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ClientType getType() {
    return type;
  }

  public void setType(ClientType type) {
    this.type = type;
  }

  public boolean isGrayListed() {
    return grayListed;
  }

  public void setGrayListed(boolean grayListed) {
    this.grayListed = grayListed;
  }
}
