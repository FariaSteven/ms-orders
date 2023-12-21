CREATE TABLE orders (
  id serial PRIMARY KEY,
  datetime timestamp NOT NULL,
  status varchar(255) NOT NULL
);
