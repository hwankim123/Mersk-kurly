CREATE TABLE orders
(
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    member_id bigint NOT NULL,
    address VARCHAR(200) NOT NULL,
    order_status VARCHAR(50) NOT NULL,
    created_at datetime(6) NOT NULL,
    updated_at datetime(6) DEFAULT NULL,
    foreign key (member_id) references member(id),
    CONSTRAINT fk_orders_to_member foreign key (member_id) references member(id) ON DELETE CASCADE
);