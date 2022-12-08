package com.prgrms.merskkurly.domain.order.repository;

import com.prgrms.merskkurly.domain.order.entity.Order;
import com.prgrms.merskkurly.domain.order.entity.OrderStatus;
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
public class OrderRepository {
  private static final String FIND_BY_ID = "select * from orders where id = :id";
  private static final String FIND_BY_MEMBER_ID = "select * from orders where member_id = :member_id";
  private static final String FIND_BY_MEMBER_ID_AND_ORDER_STATUS = "select * from orders where member_id = :member_id and order_status = :order_status";
  private static final String INSERT = "INSERT INTO orders(member_id, address, order_status, created_at, updated_at) VALUES(:member_id, :address, :order_status, :created_at, :updated_at)";
  private static final String UPDATE = "update orders set address = :address where id = :id";
  private static final String UPDATE_ORDER_STATUS = "update order set order_status = :order_status where id = :id";
  private static final String DELETE = "delete from orders where id = :id";

  private static final String ID = "id";
  private static final String MEMBER_ID = "member_id";
  private static final String ADDRESS = "address";
  private static final String ORDER_STATUS = "order_status";
  private static final String CREATED_AT = "created_at";
  private static final String UPDATED_AT = "updated_at";

  private final NamedParameterJdbcTemplate jdbcTemplate;

  public OrderRepository(DataSource dataSource) {
    jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
  }

  private final RowMapper<Order> ROW_MAPPER = (resultSet, rowNum) -> {
    Long id = resultSet.getLong(ID);
    Long memberId = resultSet.getLong(MEMBER_ID);
    String address = resultSet.getString(ADDRESS);
    OrderStatus orderStatus = OrderStatus.valueOf(resultSet.getString(ORDER_STATUS));
    LocalDateTime createdAt = resultSet.getTimestamp(CREATED_AT).toLocalDateTime();
    LocalDateTime updatedAt = resultSet.getTimestamp(UPDATED_AT).toLocalDateTime();
    return Order.getInstance(id, memberId, address, orderStatus, createdAt, updatedAt);
  };

  public Order save(Order order) {
    GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
    Map<String, Object> parameters = Map.of(
        MEMBER_ID, order.getMemberId(),
        ADDRESS, order.getAddress(),
        ORDER_STATUS, order.getOrderStatus().toString(),
        CREATED_AT, order.getCreatedAt(),
        UPDATED_AT, order.getUpdatedAt()
    );

    int update = jdbcTemplate.update(INSERT, new MapSqlParameterSource(parameters),
        generatedKeyHolder);

    Long id = Objects.requireNonNull(generatedKeyHolder.getKeyAs(BigInteger.class)).longValue();
    return Order.getInstance(
        id,
        order.getMemberId(),
        order.getAddress(),
        order.getOrderStatus(),
        order.getCreatedAt(),
        order.getUpdatedAt());
  }

  public Optional<Order> findById(Long memberId, Long orderId) {
    Map<String, Object> parameters = Map.of(
        ID, orderId,
        MEMBER_ID, memberId
    );

    return Optional.ofNullable(jdbcTemplate.queryForObject(
        FIND_BY_ID,
        parameters,
        ROW_MAPPER));
  }

  public List<Order> findByMemberId(Long memberId){
    return jdbcTemplate.queryForList(
        FIND_BY_MEMBER_ID,
        Collections.singletonMap(MEMBER_ID, memberId),
        Order.class);
  }

  public List<Order> findByMemberIdAndOrderStatus(Long memberId, OrderStatus orderStatus){
    Map<String, Object> parameters = Map.of(
        MEMBER_ID, memberId,
        ORDER_STATUS, orderStatus
    );

    return jdbcTemplate.queryForList(
        FIND_BY_MEMBER_ID_AND_ORDER_STATUS,
        parameters,
        Order.class);
  }

  public void update(Order order) {
    Map<String, Object> parameters = Map.of(
        ADDRESS, order.getAddress(),
        ID, order.getId());
    int update = jdbcTemplate.update(UPDATE, parameters);
//        if (update == ZERO) {
//            throw new DataModifyingException(
//                "Nothing was updated. query: " + UPDATE + " params: " + Member.getUUID() + ", " + Member.getDiscountAmount(),
//                CommonErrorCode.DATA_MODIFYING_ERROR);
//        }
  }

  public void updateOrderStatus(Order order){
    Map<String, Object> parameters = Map.of(
        ORDER_STATUS, order.getOrderStatus().toString(),
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
