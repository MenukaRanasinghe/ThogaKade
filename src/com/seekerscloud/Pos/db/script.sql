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

create table if not exists Order(
    orderId varchar(45),
    date varchar(250),

);

