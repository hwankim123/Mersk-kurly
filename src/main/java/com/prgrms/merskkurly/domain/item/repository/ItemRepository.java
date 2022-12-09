package com.prgrms.merskkurly.domain.item.repository;

import com.prgrms.merskkurly.domain.item.entity.Category;
import com.prgrms.merskkurly.domain.item.entity.Item;
import com.prgrms.merskkurly.domain.item.repository.ItemSearchQuery.ItemSearchQueryBuilder;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ItemRepository {

    private static final String FIND_BY_ID = "select * from item where id = :id";
    private static final String FIND_BY_MEMBER_ID = "select * from item where member_id = :member_id";
    private static final String FIND_BY_BUY_CNT_LIMIT = "select * from item ORDER BY buy_cnt LIMIT :limit"; // 이거 맞음? & buy_cnt 인덱싱 업그레이드
    private static final String INSERT = "INSERT INTO item(member_id, name, category, description, price, stock, buy_cnt, created_at, updated_at) VALUES(:member_id, :name, :category, :description, :price, :stock, :buy_cnt, :created_at, :updated_at)";
    private static final String UPDATE = "update item set name = :name, category = :category, description = :description, price = :price, stock = :stock where id = :id";
    private static final String DELETE = "delete from item where id = :id";

    private static final String ID = "id";
    private static final String MEMBER_ID = "member_id";
    private static final String NAME = "name";
    private static final String CATEGORY = "category";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String STOCK = "stock";
    private static final String BUY_CNT = "buy_cnt";
    private static final String CREATED_AT = "created_at";
    private static final String UPDATED_AT = "updated_at";

    private static final String LIMIT = "limit";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ItemRepository(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private final RowMapper<Item> ROW_MAPPER = (resultSet, rowNum) -> {
        Long id = resultSet.getLong(ID);
        Long memberId = resultSet.getLong(MEMBER_ID);
        String name = resultSet.getString(NAME);
        Category category = Category.valueOf(resultSet.getString(CATEGORY));
        String description = resultSet.getString(DESCRIPTION);
        int price = resultSet.getInt(PRICE);
        int stock = resultSet.getInt(STOCK);
        int buyCnt = resultSet.getInt(BUY_CNT);
        LocalDateTime createdAt = resultSet.getTimestamp(CREATED_AT).toLocalDateTime();
        LocalDateTime updatedAt = resultSet.getTimestamp(UPDATED_AT).toLocalDateTime();
        return Item.getInstance(id, memberId, name, category, description, price, stock, buyCnt,
                createdAt, updatedAt);
    };

    public Item save(Item item) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        Map<String, Object> parameters = Map.of(
                MEMBER_ID, item.getMemberId(),
                NAME, item.getName(),
                CATEGORY, item.getCategory().toString(),
                DESCRIPTION, item.getDescription(),
                PRICE, item.getPrice(),
                STOCK, item.getStock(),
                BUY_CNT, item.getBuyCnt(),
                CREATED_AT, item.getCreatedAt(),
                UPDATED_AT, item.getUpdatedAt()
        );

        jdbcTemplate.update(INSERT, new MapSqlParameterSource(parameters),
                generatedKeyHolder);

        Long id = Objects.requireNonNull(generatedKeyHolder.getKeyAs(BigInteger.class)).longValue();
        return Item.getInstance(
                id,
                item.getMemberId(),
                item.getName(),
                item.getCategory(),
                item.getDescription(),
                item.getPrice(),
                item.getStock(),
                item.getBuyCnt(),
                item.getCreatedAt(),
                item.getUpdatedAt());
    }

    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                FIND_BY_ID,
                Collections.singletonMap(ID, id),
                ROW_MAPPER));
    }

    public List<Item> findByBuyCntLimit(int limit) {
        return jdbcTemplate.query(
                FIND_BY_BUY_CNT_LIMIT,
                Collections.singletonMap(LIMIT, limit),
                ROW_MAPPER);
    }

    public List<Item> findByMemberId(Long memberId) {
        return jdbcTemplate.query(
                FIND_BY_MEMBER_ID,
                Collections.singletonMap(MEMBER_ID, memberId),
                ROW_MAPPER);
    }

    public void update(Item item) {
        Map<String, Object> parameters = Map.of(
                NAME, item.getName(),
                CATEGORY, item.getCategory().toString(),
                DESCRIPTION, item.getDescription(),
                PRICE, item.getPrice(),
                STOCK, item.getStock(),
                ID, item.getId());
        jdbcTemplate.update(UPDATE, parameters);
    }

    public void delete(Long id) {
        jdbcTemplate.update(DELETE, Collections.singletonMap(ID, id.toString()));
    }

    public List<Item> search(String keyword, Category category) {
        ItemSearchQuery sql = new ItemSearchQueryBuilder()
                .keyword(keyword)
                .category(category)
                .build();

        return jdbcTemplate.query(sql.getQuery(), ROW_MAPPER);
    }
}
