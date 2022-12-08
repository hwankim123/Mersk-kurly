CREATE TABLE order_item
(
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    member_id bigint NOT NULL,
    item_id bigint NOT NULL,
    order_id bigint DEFAULT 0,
    quantity bigint DEFAULT 1,
    is_ordered boolean DEFAULT false,
    created_at datetime(6) NOT NULL,
    updated_at datetime(6) DEFAULT NULL,
    CONSTRAINT fk_order_item_to_member foreign key (member_id) references member(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_item_to_orders foreign key (order_id) references orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_item_to_item foreign key (item_id) references item(id) ON DELETE CASCADE
);