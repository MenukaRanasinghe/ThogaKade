package com.seekerscloud.Pos.db;

import com.seekerscloud.Pos.modal.Customer;
import com.seekerscloud.Pos.modal.Item;
import com.seekerscloud.Pos.modal.Order;

import java.util.ArrayList;

public class Database {
    public static ArrayList<Customer> customerTable=new ArrayList<Customer>();
    public static ArrayList<Item> itemTable=new ArrayList<Item>();
    public static ArrayList<Order> orderTable=new ArrayList<>();

    static {
        customerTable.add(new Customer("c001","bandara","colombo",25000));
        customerTable.add(new Customer("c002","jayantha","kaluthara",30000));
        customerTable.add(new Customer("c003","saman","galle",50000));
        customerTable.add(new Customer("c004","kamal","jaffna",45000));
        customerTable.add(new Customer("c005","nimal","kandy",30000));

        itemTable.add(new Item("i1","descripton1",200,5));
        itemTable.add(new Item("i2","descripton2",550,10));
        itemTable.add(new Item("i3","descripton3",300,50));
        itemTable.add(new Item("i4","descripton4",100,15));
        itemTable.add(new Item("i5","descripton5",250,55));
    }

}
