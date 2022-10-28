show databases;
drop database if exists Thogakade;
create database if not exists Thogakade;
show databases;
use Thogakade;
#=========
create table if not exists Customer(
    id varchar(45),
    name varchar(45),
    address text,
    salary double,
    constraint primary key(id)
);
#==========
desc customer;
select * from customer;
create table if not exists Item(
    code varchar(45),
    description varchar(45),
    unitPrice double,
    qtyOnHand int,
    constraint primary key (code)
);
desc Item;
select * from Item;

create table if not exists orders(
    orderId varchar(45),
    date varchar(250),
    totalCost double,
    customer varchar(45),
    constraint primary key (orderId),
    constraint foreign key (customer) references Customer(id) on DELETE cascade on UPDATE cascade

);
use Thogakade;
show tables ;
select * from orders;
delete from orders where orderId='D-10';

create table if not exists orders_details(
  itemCode varchar(45),
  orderId varchar(45),
  unitPrice double,
  qty int,
  constraint primary key (itemCode,orderId),
  constraint foreign key (itemCode) references Item(code) on DELETE cascade on UPDATE cascade ,
  constraint foreign key (orderId) references orders(orderId) on delete cascade on UPDATE cascade
);
select * from Item;
select * from orders_details;
delete  from orders_details where itemCode="i2";
delete  from orders where customer="c003";



