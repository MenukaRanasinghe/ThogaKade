package com.seekerscloud.Pos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.seekerscloud.Pos.db.Database;
import com.seekerscloud.Pos.modal.Customer;
import com.seekerscloud.Pos.view.tm.CustomerTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
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
    public AnchorPane customerFormContext;
    public JFXTextField txtSearch;

    private String searchText="";

    public void initialize(){
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colOptions.setCellValueFactory(new PropertyValueFactory<>("btn"));

        searchCustomers(searchText);

        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null){
                setData(newValue);
            }

        });
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText=newValue;
            searchCustomers(searchText);
        });
    }
    private void setData(CustomerTm tm){
        txtID.setText(tm.getId());
        txtName.setText(tm.getName());
        txtAddress.setText(tm.getAddress());
        txtSalary.setText(String.valueOf(tm.getSalary()));
        btnSaveCustomer.setText("Update Customer");
    }
    private void searchCustomers(String text){
        ObservableList<CustomerTm> tmList= FXCollections.observableArrayList();
        for (Customer c:Database.customerTable
             ) {
            if (c.getName().contains(text) || c.getAddress().contains(text)){
                Button btn=new Button("Delete");
                CustomerTm tm=new CustomerTm(c.getId(),c.getName(),c.getAddress(),c.getSalary(),btn);
                tmList.add(tm);

                btn.setOnAction(event -> {
                    Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Are You Sure?", ButtonType.YES,ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get()==ButtonType.YES){
                        boolean isDeleted=Database.customerTable.remove(c);
                        if (isDeleted){
                            searchCustomers(searchText);
                            clearFields();
                            new Alert(Alert.AlertType.INFORMATION,"Customer Deleted !").show();
                        }
                        else {
                            new Alert(Alert.AlertType.WARNING,"Try Again !").show();
                        }
                    }

                });
            }

        }
        tblCustomer.setItems(tmList);
    }

    public void saveCustomerOnAction(ActionEvent actionEvent) {
        Customer c1=new Customer(txtID.getText(),txtName.getText(),txtAddress.getText(),Double.parseDouble(txtSalary.getText()));

        if (btnSaveCustomer.getText().equalsIgnoreCase("save customer")){
            boolean isSaved=Database.customerTable.add(c1);
            if (isSaved){
                searchCustomers(searchText);
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
                    searchCustomers(searchText);
                    new Alert(Alert.AlertType.INFORMATION,"Customer Updated").show();
                    clearFields();
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

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        customerFormContext.getScene().getWindow();
        Stage stage=(Stage) customerFormContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/DashboardForm.fxml"))));
    }

    public void newCustomerOnAction(ActionEvent actionEvent) {
        btnSaveCustomer.setText("Save Customer");
    }
}
