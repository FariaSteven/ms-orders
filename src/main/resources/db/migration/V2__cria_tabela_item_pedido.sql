CREATE TABLE orderitem (
  id serial PRIMARY KEY,
  description varchar(255),
  quantity int NOT NULL,
  order_id bigint NOT NULL,
  FOREIGN KEY (order_id) REFERENCES orders(id)
);