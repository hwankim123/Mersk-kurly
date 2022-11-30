package com.prgrms.merskkurly.domain.member.repository;

import com.prgrms.merskkurly.domain.member.entity.Member;
import com.prgrms.merskkurly.domain.member.entity.Role;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class MemberRepository {

    private static final String INSERT = "INSERT INTO member(name, username, password, role, address, created_at, updated_at) VALUES(:name, :username, :password, :role, :address, :created_at, :updated_at)";
    private static final String FIND_BY_ID = "select * from member where id = :id";
    private static final String FIND_ALL = "select * from member";
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

    private static final int ZERO = 0;

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

    public void save(Member Member) {
        Map<String, Object> parameters = Map.of(
                ID, Member.getId().toString(),
                NAME, Member.getName(),
                USERNAME, Member.getUsername(),
                PASSWORD, Member.getPassword(),
                ROLE, Member.getRole().toString(),
                ADDRESS, Member.getAddress(),
                CREATED_AT, Member.getCreatedAt(),
                UPDATED_AT, Member.getUpdatedAt());
        jdbcTemplate.update(INSERT, parameters);
    }

//    public List<Member> findAll() {
//        return jdbcTemplate.query(FIND_ALL, ROW_MAPPER);
//    }

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
        int update = jdbcTemplate.update(UPDATE, parameters);
//        if (update == ZERO) {
//            throw new DataModifyingException(
//                "Nothing was updated. query: " + UPDATE + " params: " + Member.getUUID() + ", " + Member.getDiscountAmount(),
//                CommonErrorCode.DATA_MODIFYING_ERROR);
//        }
    }

    public void delete(Long id) {
        int update = jdbcTemplate.update(DELETE, Collections.singletonMap(ID, id.toString()));
//        if (update == ZERO) {
//            throw new DataModifyingException("Nothing was deleted. query: " + DELETE + " params: " + id
//                    , CommonErrorCode.DATA_MODIFYING_ERROR);
//        }
    }
}
