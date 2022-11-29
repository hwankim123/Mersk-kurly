package com.prgrms.merskkurly.domain.member.entity;

import com.prgrms.merskkurly.domain.common.exception.domain.ArgumentOutOfBoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    /**
     * 회원가입 : 신규회원 객체를 생성
     *  - validation에 실패하면 생성 실패
     * 기존 데이터로부터 회원 객체를 생성
     * 회원 정보를 수정할 수 있음
     *  - password, role, address를 수정할 수 있고, updatedAt은 수정에 성공할 시 무조건 수정됨
     */

    @ParameterizedTest
    @EnumSource(value = Role.class)
    @DisplayName("신규회원 객체를 생성할 수 있습니다")
    void newMember(Role role) {
        //given
        String name = "name";
        String username = "username";
        String password = "password123!";
        String address = "경기도 성남시 분당구 판교동 111-222";

        //when
        Member member = Member.newInstance(name, username, password, role, address);

        //then
        assertThat(member.getName()).isEqualTo(name);
        assertThat(member.getUsername()).isEqualTo(username);
        assertThat(member.getPassword()).isEqualTo(password);
        assertThat(member.getRole()).isEqualTo(role);
        assertThat(member.getAddress()).isEqualTo(address);
    }

    @ParameterizedTest
    @EnumSource(value = Role.class)
    @DisplayName("기존 데이터로부터 회원 객체를 생성할 수 있습니다")
    void getMember(Role roleParameter) {
        //given
        Member member = newMemberGenerator(roleParameter);
        Long id = member.getId();
        String name = member.getName();
        String username = member.getUsername();
        String password = member.getPassword();
        Role role = member.getRole();
        String address = member.getAddress();
        LocalDateTime createdAt = member.getCreatedAt();
        LocalDateTime updatedAt = member.getUpdatedAt();

        //when
        Member getMember = Member.getInstance(id, name, username, password, role, address, createdAt, updatedAt);

        //then
        assertThat(getMember.getId()).isEqualTo(id);
        assertThat(getMember.getName()).isEqualTo(name);
        assertThat(getMember.getUsername()).isEqualTo(username);
        assertThat(getMember.getPassword()).isEqualTo(password);
        assertThat(getMember.getRole()).isEqualTo(role);
        assertThat(getMember.getAddress()).isEqualTo(address);
        assertThat(getMember.getCreatedAt()).isEqualTo(createdAt);
        assertThat(getMember.getUpdatedAt()).isEqualTo(updatedAt);
    }

    private Member newMemberGenerator(Role role){
        String name = "name";
        String username = "username";
        String password = "password123!";
        String address = "경기도 성남시 분당구 판교동 111-222";
        return Member.newInstance(name, username, password, role, address);
    }

    @Test
    @DisplayName("회원 정보를 수정할 수 있습니다")
    void updateMember() throws InterruptedException {
        //given
        Member member = newMemberGenerator(Role.USER);
        LocalDateTime beforeUpdatedAt = member.getUpdatedAt();

        //when
        Thread.sleep(100);

        String newPassword = "newPassword123!";
        Role newRole = Role.ADMIN;
        String newAddress = "새로운 주소 123-456";
        member.update(newPassword, newRole, newAddress);

        //then
        assertThat(member.getPassword()).isEqualTo(newPassword);
        assertThat(member.getRole()).isEqualTo(newRole);
        assertThat(member.getAddress()).isEqualTo(newAddress);
        assertThat(member.getUpdatedAt()).isNotEqualTo(beforeUpdatedAt);
    }

    @ParameterizedTest
    @CsvSource(value = {"김", "김환김환김환김환김환김"})
    @DisplayName("이름 길이가 2글자에서 10글자 사이가 아니면 신규 회원 생성에 실패합니다")
    void memberCreateFailureNameLength(String name) {
        //given
        String username = "username";
        String password = "password123!";
        Role role = Role.USER;
        String address = "경기도 성남시 분당구 판교동 111-222";

        //when & then
        assertThrows(ArgumentOutOfBoundException.class, () -> Member.newInstance(name, username, password, role, address));
    }

    @ParameterizedTest
    @CsvSource(value = {"abcde", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
    @DisplayName("아이디 길이가 영문 6글자에서 50글자 사이가 아니면 신규 회원 생성에 실패합니다")
    void memberCreateFailureUsernameLength(String username) {
        //given
        String name = "name";
        String password = "password123!";
        Role role = Role.USER;
        String address = "경기도 성남시 분당구 판교동 111-222";

        //when & then
        assertThrows(ArgumentOutOfBoundException.class, () -> Member.newInstance(name, username, password, role, address));
    }

    @ParameterizedTest
    @CsvSource(value = {"passw", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
    @DisplayName("비밀번호 길이가 영문 6글자에서 50글자 사이가 아니면 신규 회원 생성에 실패합니다")
    void memberCreateFailurePasswordLength(String password) {
        //given
        String name = "name";
        String username = "username";
        Role role = Role.USER;
        String address = "경기도 성남시 분당구 판교동 111-222";

        //when & then
        assertThrows(ArgumentOutOfBoundException.class, () -> Member.newInstance(name, username, password, role, address));
    }


    @ParameterizedTest
    @CsvSource(value = {"성남시 분당구 판교동", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
    @DisplayName("주소가 100글자 이상으로 이루어져 있으면 신규 회원 생성에 실패합니다")
    void memberCreateFalseAddressLength(String address) {
        //given
        String name = "name";
        String username = "username";
        String password = "password123!";
        Role role = Role.USER;

        //when & then
        assertThrows(ArgumentOutOfBoundException.class, () -> Member.newInstance(name, username, password, role, address));
    }
}