package com.prgrms.merskkurly.domain.member.repository;

import com.prgrms.merskkurly.domain.member.entity.Member;
import com.prgrms.merskkurly.domain.member.entity.Role;

import java.math.BigInteger;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class MemberRepository {

    private static final String INSERT = "INSERT INTO member(name, username, password, role, address, created_at, updated_at) VALUES(:name, :username, :password, :role, :address, :created_at, :updated_at)";
    private static final String FIND_BY_ID = "select * from member where id = :id";
    private static final String FIND_BY_USERNAME = "select * from member where username = :username";
    private static final String UPDATE = "update member set password = :password, role = :role, address = :address where id = :id";
    private static final String DELETE = "delete from member where id = :id";

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String ROLE = "role";
    private static final String ADDRESS = "address";
    private static final String CREATED_AT = "created_at";
    private static final String UPDATED_AT = "updated_at";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public MemberRepository(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private final RowMapper<Member> ROW_MAPPER = (resultSet, rowNum) -> {
        Long id = resultSet.getLong(ID);
        String name = resultSet.getString(NAME);
        String username = resultSet.getString(USERNAME);
        String password = resultSet.getString(PASSWORD);
        Role role = Role.valueOf(resultSet.getString(ROLE));
        String address = resultSet.getString(ADDRESS);
        LocalDateTime createdAt = resultSet.getTimestamp(CREATED_AT).toLocalDateTime();
        LocalDateTime updatedAt = resultSet.getTimestamp(UPDATED_AT).toLocalDateTime();
        return Member.getInstance(id, name, username, password, role, address, createdAt, updatedAt);
    };

    public Member save(Member member) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        Map<String, Object> parameters = Map.of(
                NAME, member.getName(),
                USERNAME, member.getUsername(),
                PASSWORD, member.getPassword(),
                ROLE, member.getRole().toString(),
                ADDRESS, member.getAddress(),
                CREATED_AT, member.getCreatedAt(),
                UPDATED_AT, member.getUpdatedAt());

        jdbcTemplate.update(INSERT, new MapSqlParameterSource(parameters),
                generatedKeyHolder);

        Long id = Objects.requireNonNull(generatedKeyHolder.getKeyAs(BigInteger.class)).longValue();
        return Member.getInstance(
                id,
                member.getName(),
                member.getUsername(),
                member.getPassword(),
                member.getRole(),
                member.getAddress(),
                member.getCreatedAt(),
                member.getUpdatedAt());
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                FIND_BY_ID,
                Collections.singletonMap(ID, id),
                ROW_MAPPER));
    }

    public void update(Member member) {
        Map<String, Object> parameters = Map.of(
                PASSWORD, member.getPassword(),
                ROLE, member.getRole().toString(),
                ADDRESS, member.getAddress(),
                ID, member.getId());
        jdbcTemplate.update(UPDATE, parameters);
    }

    public void delete(Long id) {
        jdbcTemplate.update(DELETE, Collections.singletonMap(ID, id.toString()));
    }

    public Optional<Member> findByUsername(String username) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                FIND_BY_USERNAME,
                Collections.singletonMap(USERNAME, username),
                ROW_MAPPER));
    }
}
