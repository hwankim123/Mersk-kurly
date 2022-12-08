package com.prgrms.merskkurly.domain.common.dto;

import com.prgrms.merskkurly.domain.member.entity.Role;

public class AuthDto {
  private Long id;
  private String name;
  private String role;

  public AuthDto() {
  }

  public AuthDto(Long id, String name, String role) {
    this.id = id;
    this.name = name;
    this.role = role;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
