package com.prgrms.merskkurly.domain.member.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.prgrms.merskkurly.domain.member.entity.Member;
import com.prgrms.merskkurly.domain.member.entity.Role;
import java.time.LocalDateTime;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class MemberRepositoryTest {

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  JdbcTemplate jdbcTemplate;

  @BeforeEach
  void createTable(){
    jdbcTemplate.execute("DROP TABLE member IF EXISTS");
    jdbcTemplate.execute("CREATE TABLE member\n"
        + "(\n"
        + "    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,\n"
        + "    name VARCHAR(20) NOT NULL,\n"
        + "    username VARCHAR(50) NOT NULL UNIQUE,\n"
        + "    password VARCHAR(50) NOT NULL,\n"
        + "    role VARCHAR(20) NOT NULL,\n"
        + "    address VARCHAR(200) DEFAULT NULL,\n"
        + "    created_at datetime(6) NOT NULL,\n"
        + "    updated_at datetime(6) DEFAULT NULL\n"
        + ");");
  }

  @Test
  @DisplayName("회원을 추가할 수 있습니다")
  void insertMember(){
    //given
    Member member = Member.newInstance(
        "name",
        "username",
        "passwork123",
        Role.USER,
        "경기도 성남시 분당구 판교동");

    //when&then
    memberRepository.save(member);
  }

  @Test
  @DisplayName("member_id를 통해 사용자를 조회할 수 있습니다.")
  void findMemberById(){
    //given
    String name = "name";
    String username = "username";
    String password = "password123";
    Role role = Role.USER;
    String address = "경기도 성남시 분당구 판교동";
    Member member = createSingleMember(name, username, password, role, address);

    memberRepository.save(member);

    //when
    Optional<Member> findMember = memberRepository.findById(1L);

    //then
    assertThat(findMember.isPresent()).isTrue();
    assertThat(findMember.get().getName()).isEqualTo(name);
    assertThat(findMember.get().getUsername()).isEqualTo(username);
    assertThat(findMember.get().getPassword()).isEqualTo(password);
    assertThat(findMember.get().getRole()).isEqualTo(role);
    assertThat(findMember.get().getAddress()).isEqualTo(address);
  }

  private Member createSingleMember(String name, String username, String password, Role role, String address){
    return Member.newInstance(name,
        username,
        password,
        role,
        address);
  }

  @Test
  @DisplayName("사용자 정보를 수정할 수 있습니다")
  void updateMember(){
    //given
    String name = "name";
    String username = "username";
    String password = "password123";
    Role role = Role.USER;
    String address = "경기도 성남시 분당구 판교동";
    Member member = createSingleMember(name, username, password, role, address);

    memberRepository.save(member);
    Member findMember = memberRepository.findById(1L).get();

    //when
    String newPassword = "newPassword";
    Role newRole = Role.ADMIN;
    String newAddress = "새로운 주소 새로운 주소 새로운 주소 새로운 주소 ";
    findMember.update(newPassword, newRole, newAddress);
    memberRepository.update(findMember);
    Member memberAfterUpdate = memberRepository.findById(findMember.getId()).get();

    //then
    assertThat(memberAfterUpdate.getPassword()).isEqualTo(newPassword);
    assertThat(memberAfterUpdate.getRole()).isEqualTo(newRole);
    assertThat(memberAfterUpdate.getAddress()).isEqualTo(newAddress);
  }

  @Test
  @DisplayName("사용자 정보를 탈퇴할 수 있다")
  void deleteMember(){
    //given
    String name = "name";
    String username = "username";
    String password = "password123";
    Role role = Role.USER;
    String address = "경기도 성남시 분당구 판교동";
    Member member = createSingleMember(name, username, password, role, address);

    memberRepository.save(member);
    Member findMember = memberRepository.findById(1L).get();

    //when
    memberRepository.delete(findMember.getId());
    Optional<Member> memberAfterUpdate = memberRepository.findById(findMember.getId());

    //then
    assertThat(memberAfterUpdate.isEmpty()).isEqualTo(true);
  }

  //Repository.save() 가 Entity를 반환하도록
  //Repository 에서의 exception 처리
}