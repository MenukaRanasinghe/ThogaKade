package com.seekerscloud.Pos.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.seekerscloud.Pos.db.Database;
import com.seekerscloud.Pos.modal.Customer;
import com.seekerscloud.Pos.modal.Item;
import com.seekerscloud.Pos.view.tm.CartTm;
import com.seekerscloud.Pos.view.tm.ItemTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    public TableView<CartTm> tblCart;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colQuantity;
    public TableColumn colTotal;
    public TableColumn colOptions;
    public JFXTextField txtQuantity;

    public void initialize() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colOptions.setCellValueFactory(new PropertyValueFactory<>("btn"));
        /*Date date=new Date();
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        df.format(date);
        String d=df.format(date);
        txtDate.setText(d);*/
        txtDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        loadCustomerIds();
        loadItemIds();

        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
                setCustomerDetails();
            }
        });

        cmbItemId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setItemDetails();
            }
        });
    }

    private void setItemDetails() {
        for (Item i : Database.itemTable) {
            if (i.getCode().equals(cmbItemId.getValue())) {
                txtDescription.setText(i.getDescription());
                txtUnitPrice.setText(String.valueOf(i.getUnitPrice()));
                txtQtyOnHand.setText(String.valueOf(i.getQtyOnHand()));
            }
        }
    }

    private void setCustomerDetails() {
        for (Customer c : Database.customerTable) {
            if (c.getId().equals(cmbCustomerId.getValue())) {
                txtName.setText(c.getName());
                txtAddress.setText(c.getAddress());
                txtSalary.setText(String.valueOf(c.getSalary()));
            }
        }
    }

    private void loadItemIds() {
        for (Item i : Database.itemTable) {
            cmbItemId.getItems().add(i.getCode());
        }
    }

    private void loadCustomerIds() {
        for (Customer c : Database.customerTable) {
            cmbCustomerId.getItems().add(c.getId());
        }
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        placeOrderFormContext.getScene().getWindow();
        Stage stage = (Stage) placeOrderFormContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/DashboardForm.fxml"))));
    }

    ObservableList<CartTm> oblist = FXCollections.observableArrayList();

    public void addTocartOnAction(ActionEvent actionEvent) {
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        int qty = Integer.parseInt(txtQuantity.getText());
        double total = unitPrice * qty;
        Button btn = new Button("delete");

        int row = isAlreadyExists(cmbItemId.getValue());

        if (row == -1) {
            CartTm tm = new CartTm(
                    cmbItemId.getValue(),
                    txtDescription.getText(),
                    unitPrice,
                    qty,
                    total,
                    btn);


            oblist.add(tm);
            tblCart.setItems(oblist);
        } else {
            int tempQty = oblist.get(row).getQty() + qty;
            double tempTotal = unitPrice + tempQty;
            oblist.get(row).setQty(tempQty);
            oblist.get(row).setTotal(tempTotal);
            tblCart.refresh();
        }


    }

    private int isAlreadyExists(String code) {
        for (int i = 0; i < oblist.size(); i++) {
            if (oblist.get(i).getCode().equals(code)) {
                return i;
            }

        }
        return -1;
    }
}

