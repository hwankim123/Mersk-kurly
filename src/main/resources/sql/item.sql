CREATE TABLE item
(
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    member_id bigint NOT NULL,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    description VARCHAR(500) DEFAULT NULL,
    price bigint NOT NULL,
    buy_cnt bigint DEFAULT 0,
    created_at datetime(6) NOT NULL,
    updated_at datetime(6) DEFAULT NULL,
    CONSTRAINT fk_order_item_to_orders foreign key (member_id) references member(id) ON DELETE CASCADE
);