package com.seekerscloud.Pos.controller;

import com.jfoenix.controls.JFXTextField;
import com.seekerscloud.Pos.db.Database;
import com.seekerscloud.Pos.modal.Customer;
import com.seekerscloud.Pos.view.tm.CustomerTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Arrays;

public class CustomerFormController {
    public JFXTextField txtID;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtSalary;
    public TableView<CustomerTm> tblCustomer;
    public TableColumn colID;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colSalary;
    public TableColumn colOptions;

    public void initialize(){
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colOptions.setCellValueFactory(new PropertyValueFactory<>("btn"));

        searchCustomers();
    }
    private void searchCustomers(){
        ObservableList<CustomerTm> tmList= FXCollections.observableArrayList();
        for (Customer c:Database.customerTable
             ) {
            Button btn=new Button("Delete");
            CustomerTm tm=new CustomerTm(c.getId(),c.getName(),c.getAddress(),c.getSalary(),btn);
            tmList.add(tm);
        }
        tblCustomer.setItems(tmList);
    }

    public void saveCustomerOnAction(ActionEvent actionEvent) {
        Customer c1=new Customer(txtID.getText(),txtName.getText(),txtAddress.getText(),Double.parseDouble(txtSalary.getText()));
        boolean isSaved=Database.customerTable.add(c1);
        if (isSaved){
            searchCustomers();
            new Alert(Alert.AlertType.INFORMATION,"Customer Saved !").show();
        }
        else {
            new Alert(Alert.AlertType.WARNING,"Try Again !").show();
        }
    }
}
