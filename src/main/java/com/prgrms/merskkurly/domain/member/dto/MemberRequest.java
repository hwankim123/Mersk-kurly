package com.prgrms.merskkurly.domain.member.dto;

public class MemberRequest {

  public static class SignupForm{
    private String name;
    private String username;
    private String password;
    private String role;
    private String address;

    public SignupForm(String name, String username, String password, String role, String address) {
      this.name = name;
      this.username = username;
      this.password = password;
      this.role = role;
      this.address = address;
    }

    public SignupForm() {

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

    public String getRole() {
      return role;
    }

    public void setRole(String role) {
      this.role = role;
    }

    public String getAddress() {
      return address;
    }

    public void setAddress(String address) {
      this.address = address;
    }
  }

  public static class LoginForm{
    private String username;
    private String password;

    public LoginForm(String username, String password) {
      this.username = username;
      this.password = password;
    }

    public LoginForm() {}

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
  }

  public static class UpdateForm {
    private String password;
    private String role;
    private String address;

    public UpdateForm(){}

    public UpdateForm(String password, String role, String address) {
      this.password = password;
      this.role = role;
      this.address = address;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    public String getRole() {
      return role;
    }

    public void setRole(String role) {
      this.role = role;
    }

    public String getAddress() {
      return address;
    }

    public void setAddress(String address) {
      this.address = address;
    }
  }
}
