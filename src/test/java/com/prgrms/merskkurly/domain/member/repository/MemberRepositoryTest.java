package com.prgrms.merskkurly.domain.member.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.prgrms.merskkurly.domain.member.entity.Member;
import com.prgrms.merskkurly.domain.member.entity.Role;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class MemberRepositoryTest {

  Logger logger = LoggerFactory.getLogger("MemberRepositoryTest");

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

    //when
    Member savedMember = memberRepository.save(member);

    //then
    logger.info(savedMember.getId().toString());
    assertThat(savedMember.getName()).isEqualTo(member.getName());
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

    Member savedMember = memberRepository.save(member);

    //when
    Optional<Member> findMember = memberRepository.findById(savedMember.getId());

    //then
    assertThat(findMember).isPresent();
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

    Member savedMember = memberRepository.save(member);
    Member findMember = memberRepository.findById(savedMember.getId()).get();

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

    Member savedMember = memberRepository.save(member);
    Member findMember = memberRepository.findById(savedMember.getId()).get();
    //when
    memberRepository.delete(findMember.getId());

    //then
    assertThrows(EmptyResultDataAccessException.class, () -> memberRepository.findById(findMember.getId()));
  }

  //Repository 에서의 exception 처리
}