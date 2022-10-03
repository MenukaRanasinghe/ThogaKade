package com.seekerscloud.Pos.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.seekerscloud.Pos.db.Database;
import com.seekerscloud.Pos.modal.Customer;
import com.seekerscloud.Pos.modal.Item;
import javafx.event.ActionEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PlaceOrderFormController {
    public JFXTextField txtDate;
    public JFXComboBox<String> cmbCustomerId;
    public JFXComboBox<String> cmbItemId;

    public void initialize(){
        /*Date date=new Date();
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        df.format(date);
        String d=df.format(date);
        txtDate.setText(d);*/
        txtDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        loadCustomerIds();
        loadItemIds();
    }

    private void loadItemIds() {
        for (Item i:Database.itemTable){
            cmbItemId.getItems().add(i.getCode());
        }
    }

    private void loadCustomerIds() {
        for (Customer c:Database.customerTable){
            cmbCustomerId.getItems().add(c.getId());
        }
    }

    public void backToHomeOnAction(ActionEvent actionEvent) {
    }
}
