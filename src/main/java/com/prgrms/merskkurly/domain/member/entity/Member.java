package com.prgrms.merskkurly.domain.member.entity;

import com.prgrms.merskkurly.domain.common.exception.domain.ArgumentOutOfBoundException;

import java.time.LocalDateTime;

import static com.prgrms.merskkurly.domain.member.util.MemberValidateFields.*;

public class Member {
    private static final Long BEFORE_INITIALIZED_ID = 0L;

    private final Long id;
    private final String name;
    private final String username;
    private String password;
    private Role role;
    private String address;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Member(String name, String username){
        this.id = BEFORE_INITIALIZED_ID;
        this.name = name;
        this.username = username;
        this.createdAt = LocalDateTime.now();
    }

    private Member(Long id, String name, String username, LocalDateTime createdAt){
        this.id = id;
        this.name = name;
        this.username = username;
        this.createdAt = createdAt;
    }

    public static Member newInstance(String name, String username, String password, Role role, String address) {
        validateName(name);
        validateUsername(username);
        validatePassword(password);
        validateAddress(address);

        Member member = new Member(name, username);
        member.password = password;
        member.role = role;
        member.address = address;
        member.updatedAt = member.createdAt;
        return member;
    }

    public static Member getInstance(Long id, String name, String username, String password, Role role, String address, LocalDateTime createdAt, LocalDateTime updatedAt) {
        validateName(name);
        validateUsername(username);
        validatePassword(password);
        validateAddress(address);

        Member member = new Member(id, name, username, createdAt);
        member.password = password;
        member.role = role;
        member.address = address;
        member.updatedAt = updatedAt;
        return member;
    }

    public void update(String newPassword, Role newRole, String newAddress) {
        validatePassword(password);
        validateAddress(address);

        password = newPassword;
        role = newRole;
        address = newAddress;
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public String getAddress() {
        return address;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    private static void validateAddress(String address) {
        if(address.length() < MIN_ADDRESS|| MAX_ADDRESS < address.length()){
            throw new ArgumentOutOfBoundException(
                    ADDRESS_FIELD_NAME, MIN_ADDRESS, MAX_ADDRESS, address.length());
        }
    }

    private static void validatePassword(String password) {
        if(password.length() < MIN_PASSWORD|| MAX_PASSWORD < password.length()){
            throw new ArgumentOutOfBoundException(
                    PASSWORD_FIELD_NAME, MIN_PASSWORD, MAX_PASSWORD, password.length());
        }
    }

    private static void validateUsername(String username) {
        if(username.length() < MIN_USERNAME || MAX_USERNAME < username.length()){
            throw new ArgumentOutOfBoundException(
                    USERNAME_FIELD_NAME, MIN_USERNAME, MAX_USERNAME, username.length());
        }
    }

    private static void validateName(String name) {
        if(name.length() < MIN_NAME || MAX_NAME < name.length()){
            throw new ArgumentOutOfBoundException(
                    NAME_FIELD_NAME, MIN_NAME, MAX_NAME, name.length());
        }
    }

    public void verifyPassword(String inputPassword) {
        if (!password.equals(inputPassword)) {
            throw new IllegalArgumentException("wrong username of passwowrd");
        }
    }
}
