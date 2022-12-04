CREATE TABLE order_item
(
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    item_id bigint NOT NULL,
    order_id bigint NOT NULL,
    quantity bigint DEFAULT 1,
    FOREIGN KEY (item_id) references item_id(id),
    CONSTRAINT fk_order_item_to_orders foreign key (order_id) references orders(id) ON DELETE CASCADE
);