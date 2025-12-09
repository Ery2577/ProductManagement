create table Product (
    id serial primary key,
    name varchar(300) not null,
    price numeric(10, 2) not null,
    creation_datetime timestamp without time zone not null
);

create table Product_category (
    id serial primary key,
    name varchar (300) not null,
    product_id int not null,
    foreign key (product_id) references Product(id)
);