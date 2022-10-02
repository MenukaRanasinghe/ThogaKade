package com.seekerscloud.Pos.controller;

import com.jfoenix.controls.JFXTextField;
import com.seekerscloud.Pos.db.Database;
import com.seekerscloud.Pos.modal.Customer;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public class CustomerFormController {
    public JFXTextField txtID;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtSalary;

    public void saveCustomerOnAction(ActionEvent actionEvent) {
        Customer c1=new Customer(txtID.getText(),txtName.getText(),txtAddress.getText(),Double.parseDouble(txtSalary.getText()));
        boolean isSaved=Database.customerTable.add(c1);
        if (isSaved){
            new Alert(Alert.AlertType.INFORMATION,"Customer Saved !").show();
        }
        else {
            new Alert(Alert.AlertType.WARNING,"Try Again !").show();
        }
    }
}
