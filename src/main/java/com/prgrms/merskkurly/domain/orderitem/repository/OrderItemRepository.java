package com.prgrms.merskkurly.domain.orderitem.repository;

import com.prgrms.merskkurly.domain.orderitem.entity.OrderItem;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
  private static final String FIND_BY_ORDER_ID = "select * from order where order_id = :orderId";
  private static final String FIND_BY_ITEM_ID = "select * from order where item_id = :itemId";
  private static final String INSERT = "INSERT INTO order_item(order_id, item_id, quantity, created_at, updated_at) VALUES(:orderId, :itemId, :quantity, :createdAt, :updatedAt)";
  private static final String UPDATE = "update order_item set quantity = :quantity where id = :id";
  private static final String DELETE = "delete from order_item where id = :id";

  private static final String ID = "id";
  private static final String ORDER_ID = "order_id";
  private static final String ITEM_ID = "item_id";
  private static final String QUANTITY = "quantity";
  private static final String CREATED_AT = "created_at";
  private static final String UPDATED_AT = "updated_at";

  private final NamedParameterJdbcTemplate jdbcTemplate;

  public OrderItemRepository(DataSource dataSource) {
    jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
  }

  private final RowMapper<OrderItem> ROW_MAPPER = (resultSet, rowNum) -> {
    Long id = resultSet.getLong(ID);
    Long orderId = resultSet.getLong(ORDER_ID);
    Long itemId = resultSet.getLong(ITEM_ID);
    int quantity = resultSet.getInt(QUANTITY);
    LocalDateTime createdAt = resultSet.getTimestamp(CREATED_AT).toLocalDateTime();
    LocalDateTime updatedAt = resultSet.getTimestamp(UPDATED_AT).toLocalDateTime();
    return com.prgrms.merskkurly.domain.orderitem.entity.OrderItem.getInstance(id, orderId, itemId, quantity, createdAt, updatedAt);
  };

  public OrderItem save(
      com.prgrms.merskkurly.domain.orderitem.entity.OrderItem orderItem) {
    GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
    Map<String, Object> parameters = Map.of(
        ORDER_ID, orderItem.getOrderId(),
        ITEM_ID, orderItem.getItemId(),
        QUANTITY, orderItem.getQuantity(),
        CREATED_AT, orderItem.getCreatedAt(),
        UPDATED_AT, orderItem.getUpdatedAt()
    );

    int update = jdbcTemplate.update(INSERT, new MapSqlParameterSource(parameters),
        generatedKeyHolder);

    Long id = generatedKeyHolder.getKeyAs(Long.class);
    return com.prgrms.merskkurly.domain.orderitem.entity.OrderItem.getInstance(
        id,
        orderItem.getOrderId(),
        orderItem.getItemId(),
        orderItem.getQuantity(),
        orderItem.getCreatedAt(),
        orderItem.getUpdatedAt());
  }

  public Optional<OrderItem> findById(Long id) {
    return Optional.ofNullable(jdbcTemplate.queryForObject(
        FIND_BY_ID,
        Collections.singletonMap(ID, id),
        ROW_MAPPER));
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
