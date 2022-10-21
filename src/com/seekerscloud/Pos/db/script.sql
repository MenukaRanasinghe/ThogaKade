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

