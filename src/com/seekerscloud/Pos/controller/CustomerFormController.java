package com.seekerscloud.Pos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.seekerscloud.Pos.db.Database;
import com.seekerscloud.Pos.modal.Customer;
import com.seekerscloud.Pos.view.tm.CustomerTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Arrays;
import java.util.Optional;

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
    public JFXButton btnSaveCustomer;

    public void initialize(){
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colOptions.setCellValueFactory(new PropertyValueFactory<>("btn"));

        searchCustomers();

        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setData(newValue);
        });
    }
    private void setData(CustomerTm tm){
        txtID.setText(tm.getId());
        txtName.setText(tm.getName());
        txtAddress.setText(tm.getAddress());
        txtSalary.setText(String.valueOf(tm.getSalary()));
        btnSaveCustomer.setText("Update Customer");
    }
    private void searchCustomers(){
        ObservableList<CustomerTm> tmList= FXCollections.observableArrayList();
        for (Customer c:Database.customerTable
             ) {
            Button btn=new Button("Delete");
            CustomerTm tm=new CustomerTm(c.getId(),c.getName(),c.getAddress(),c.getSalary(),btn);
            tmList.add(tm);

            btn.setOnAction(event -> {
                Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Are You Sure?", ButtonType.YES,ButtonType.NO);
                Optional<ButtonType> buttonType = alert.showAndWait();
                if (buttonType.get()==ButtonType.YES){
                    boolean isDeleted=Database.customerTable.remove(c);
                    if (isDeleted){
                        searchCustomers();
                        clearFields();
                        new Alert(Alert.AlertType.INFORMATION,"Customer Deleted !").show();
                    }
                    else {
                        new Alert(Alert.AlertType.WARNING,"Try Again !").show();
                    }
                }

            });
        }
        tblCustomer.setItems(tmList);
    }

    public void saveCustomerOnAction(ActionEvent actionEvent) {
        Customer c1=new Customer(txtID.getText(),txtName.getText(),txtAddress.getText(),Double.parseDouble(txtSalary.getText()));

        if (btnSaveCustomer.getText().equalsIgnoreCase("save customer")){
            boolean isSaved=Database.customerTable.add(c1);
            if (isSaved){
                searchCustomers();
                clearFields();
                new Alert(Alert.AlertType.INFORMATION,"Customer Saved !").show();
            }
            else {
                new Alert(Alert.AlertType.WARNING,"Try Again !").show();
            }
        }
        else {
            for (int i = 0; i < Database.customerTable.size(); i++) {
                if (txtID.getText().equalsIgnoreCase(Database.customerTable.get(i).getId())){
                    Database.customerTable.get(i).setName(txtName.getText());
                    Database.customerTable.get(i).setAddress(txtAddress.getText());
                    Database.customerTable.get(i).setSalary(Double.parseDouble(txtSalary.getText()));
                    searchCustomers();
                    new Alert(Alert.AlertType.INFORMATION,"Customer Updated").show();
                }
            }
        }


    }
    private void clearFields(){
        txtID.clear();
        txtName.clear();
        txtAddress.clear();
        txtSalary.clear();
    }
}
