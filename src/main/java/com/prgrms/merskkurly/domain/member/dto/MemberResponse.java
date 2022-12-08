package com.prgrms.merskkurly.domain.member.dto;

import com.prgrms.merskkurly.domain.member.entity.Member;
import com.prgrms.merskkurly.domain.member.entity.Role;
import java.util.List;

public class MemberResponse {

  public static class LoginInfo{
    private Long id;
    private String name;
    private String username;
    private Role role;

    public LoginInfo(Long id, String name, String username, Role role) {
      this.id = id;
      this.name = name;
      this.username = username;
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

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public Role getRole() {
      return role;
    }

    public void setRole(Role role) {
      this.role = role;
    }
  }

  public static class Details{
    private String name;
    private String username;
    private String password;
    private Role role;
    private String address;

    private Details(String name, String username, String password, Role role, String address) {
      this.name = name;
      this.username = username;
      this.password = password;
      this.role = role;
      this.address = address;
    }

    public static Details from(Member member){
      return new Details(
          member.getName(),
          member.getUsername(),
          member.getPassword(),
          member.getRole(),
          member.getAddress()
      );
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    public Role getRole() {
      return role;
    }

    public void setRole(Role role) {
      this.role = role;
    }

    public String getAddress() {
      return address;
    }

    public void setAddress(String address) {
      this.address = address;
    }
  }

  public static class SignupData{
    public List<Role> roles;

    public SignupData(List<Role> roles) {
      this.roles = roles;
    }

    public List<Role> getRoles() {
      return roles;
    }

    public void setRoles(List<Role> roles) {
      this.roles = roles;
    }
  }
}
