package com.prgrms.merskkurly.domain.item.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class ItemRepositoryTest {
//  private static final String FIND_BY_CATEGORY = "select * from item where category = :category";
//  private static final String FIND_BY_KEYWORD = "select * from item where name like %:keyword%"; // like문
//  private static final String FIND_BY_ID = "select * from item where id = :id";
//  private static final String FIND_BY_MEMBER_ID = "select * from item where member_id = :memberId";
//  private static final String FIND_BY_BUY_CNT_LIMIT = "select * from item ORDER BY buy_cnt LIMIT :score"; // 이거 맞음? & buy_cnt 인덱싱 업그레이드
//  private static final String INSERT = "INSERT INTO item(member_id, name, category, description, price, stock, buy_cnt, created_at, updated_at) VALUES(:memberId, :name, :category, :description, :price, :stock, :buyCnt, :createdAt, :updatedAt)";
//  private static final String UPDATE = "update item set name = :name, category = :category, description = :description, price = :price, stock = :stock where id = :id";
//  private static final String DELETE = "delete from item where id = :id";

  Logger logger = LoggerFactory.getLogger("MemberRepositoryTest");

  @Autowired
  ItemRepository itemRepository;

  @Autowired
  JdbcTemplate jdbcTemplate;

  @BeforeEach
  void createTable(){
    jdbcTemplate.execute("DROP TABLE item IF EXISTS");
    jdbcTemplate.execute("CREATE TABLE item\n"
        + "(\n"
        + "    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,\n"
        + "    member_id bigint NOT NULL,\n"
        + "    name VARCHAR(100) NOT NULL,\n"
        + "    category VARCHAR(50) NOT NULL,\n"
        + "    description VARCHAR(500) DEFAULT NULL,\n"
        + "    price bigint NOT NULL,\n"
        + "    buy_cnt bigint DEFAULT 0,\n"
        + "    created_at datetime(6) NOT NULL,\n"
        + "    updated_at datetime(6) DEFAULT NULL,\n"
        + "    CONSTRAINT fk_order_item_to_orders foreign key (member_id) references member(id) ON DELETE CASCADE\n"
        + ");");
  }
}