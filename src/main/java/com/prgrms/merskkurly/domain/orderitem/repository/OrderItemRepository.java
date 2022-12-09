package com.prgrms.merskkurly.domain.orderitem.repository;

import com.prgrms.merskkurly.domain.orderitem.entity.OrderItem;

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
public class OrderItemRepository {
    private static final String FIND_BY_ID = "select * from order_item where id = :id";
    private static final String FIND_BY_MEMBER_ID = "select * from order_item where member_id = :member_id";
    private static final String FIND_BY_MEMBER_ID_AND_NOT_ORDERED = "select * from order_item where member_id = :member_id and is_ordered = :is_ordered";
    private static final String FIND_BY_ORDER_ID = "select * from order_item where order_id = :order_id";
    private static final String FIND_BY_ITEM_ID = "select * from order_item where item_id = :item_id";
    private static final String INSERT = "INSERT INTO order_item(member_id, item_id, order_id, quantity, is_ordered, created_at, updated_at) VALUES(:member_id, :item_id, :order_id, :quantity, :is_ordered, :created_at, :updated_at)";
    private static final String UPDATE = "update order_item set quantity = :quantity where id = :id";
    private static final String ORDER = "update order_item set order_id = :order_id, is_ordered = 1 where member_id = :member_id";
    private static final String DELETE = "delete from order_item where id = :id";

    private static final String ID = "id";
    private static final String MEMBER_ID = "member_id";
    private static final String ORDER_ID = "order_id";
    private static final String ITEM_ID = "item_id";
    private static final String QUANTITY = "quantity";
    private static final String IS_ORDERED = "is_ordered";
    private static final String CREATED_AT = "created_at";
    private static final String UPDATED_AT = "updated_at";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OrderItemRepository(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private final RowMapper<OrderItem> ROW_MAPPER = (resultSet, rowNum) -> {
        Long id = resultSet.getLong(ID);
        Long memberId = resultSet.getLong(MEMBER_ID);
        Long orderId = resultSet.getLong(ORDER_ID);
        Long itemId = resultSet.getLong(ITEM_ID);
        int quantity = resultSet.getInt(QUANTITY);
        boolean isOrdered = resultSet.getBoolean(IS_ORDERED);
        LocalDateTime createdAt = resultSet.getTimestamp(CREATED_AT).toLocalDateTime();
        LocalDateTime updatedAt = resultSet.getTimestamp(UPDATED_AT).toLocalDateTime();
        return OrderItem.getInstance(id, memberId, itemId, orderId, quantity, isOrdered, createdAt, updatedAt);
    };

    public OrderItem save(OrderItem orderItem) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        Map<String, Object> parameters = Map.of(
                MEMBER_ID, orderItem.getMemberId(),
                ITEM_ID, orderItem.getItemId(),
                ORDER_ID, orderItem.getOrderId(),
                QUANTITY, orderItem.getQuantity(),
                IS_ORDERED, orderItem.getIsOrdered(),
                CREATED_AT, orderItem.getCreatedAt(),
                UPDATED_AT, orderItem.getUpdatedAt()
        );

        jdbcTemplate.update(INSERT, new MapSqlParameterSource(parameters),
                generatedKeyHolder);

        Long id = Objects.requireNonNull(generatedKeyHolder.getKeyAs(BigInteger.class)).longValue();
        return OrderItem.getInstance(
                id,
                orderItem.getMemberId(),
                orderItem.getOrderId(),
                orderItem.getItemId(),
                orderItem.getQuantity(),
                orderItem.getIsOrdered(),
                orderItem.getCreatedAt(),
                orderItem.getUpdatedAt());
    }

    public Optional<OrderItem> findById(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                FIND_BY_ID,
                Collections.singletonMap(ID, id),
                ROW_MAPPER));
    }

    public List<OrderItem> findByMemberId(Long memberId) {
        return jdbcTemplate.query(
                FIND_BY_MEMBER_ID,
                Collections.singletonMap(MEMBER_ID, memberId),
                ROW_MAPPER);
    }

    public List<OrderItem> findByMemberIdAndNotOrdered(Long memberId) {
        Map<String, Object> parameters = Map.of(
                MEMBER_ID, memberId,
                IS_ORDERED, false
        );

        return jdbcTemplate.query(FIND_BY_MEMBER_ID_AND_NOT_ORDERED, parameters, ROW_MAPPER);
    }

    public List<OrderItem> findByOrderId(Long orderId) {
        return jdbcTemplate.queryForList(
                FIND_BY_ORDER_ID,
                Collections.singletonMap(ORDER_ID, orderId),
                OrderItem.class);
    }

    public List<OrderItem> findByItemId(Long itemId) {
        return jdbcTemplate.queryForList(
                FIND_BY_ITEM_ID,
                Collections.singletonMap(ORDER_ID, itemId),
                OrderItem.class);
    }

    public void update(OrderItem order) {
        Map<String, Object> parameters = Map.of(
                QUANTITY, order.getQuantity(),
                ID, order.getId());
        jdbcTemplate.update(UPDATE, parameters);
    }

    public void order(Long orderId, Long memberId) {
        Map<String, Object> parameters = Map.of(
                ORDER_ID, orderId,
                MEMBER_ID, memberId);
        jdbcTemplate.update(ORDER, parameters);
    }

    public void delete(Long id) {
        jdbcTemplate.update(DELETE, Collections.singletonMap(ID, id.toString()));
    }
}
