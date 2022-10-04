package com.seekerscloud.Pos.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.seekerscloud.Pos.db.Database;
import com.seekerscloud.Pos.modal.Customer;
import com.seekerscloud.Pos.modal.Item;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlaceOrderFormController {
    public JFXTextField txtDate;
    public JFXComboBox<String> cmbCustomerId;
    public JFXComboBox<String> cmbItemId;
    public AnchorPane placeOrderFormContext;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtSalary;
    public JFXTextField txtDescription;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtQtyOnHand;
    public TableView tblCart;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colQuantity;
    public TableColumn colTotal;
    public TableColumn colOptions;

    public void initialize(){
        /*Date date=new Date();
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        df.format(date);
        String d=df.format(date);
        txtDate.setText(d);*/
        txtDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        loadCustomerIds();
        loadItemIds();

        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

           if (newValue!=null){
               setCustomerDetails();
           }
        });

        cmbItemId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null){
                setItemDetails();
            }
        });
    }

    private void setItemDetails() {
        for (Item i:Database.itemTable){
            if (i.getCode().equals(cmbItemId.getValue())){
                txtDescription.setText(i.getDescription());
                txtUnitPrice.setText(String.valueOf(i.getUnitPrice()));
                txtQtyOnHand.setText(String.valueOf(i.getQtyOnHand()));
            }
        }
    }

    private void setCustomerDetails() {
        for (Customer c:Database.customerTable){
            if (c.getId().equals(cmbCustomerId.getValue())){
                txtName.setText(c.getName());
                txtAddress.setText(c.getAddress());
                txtSalary.setText(String.valueOf(c.getSalary()));
            }
        }
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

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        placeOrderFormContext.getScene().getWindow();
        Stage stage=(Stage) placeOrderFormContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/DashboardForm.fxml"))));
    }

    public void addTocartOnAction(ActionEvent actionEvent) {
    }
}
