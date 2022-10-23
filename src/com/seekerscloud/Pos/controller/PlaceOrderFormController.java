package com.seekerscloud.Pos.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.seekerscloud.Pos.db.Database;
import com.seekerscloud.Pos.modal.Customer;
import com.seekerscloud.Pos.modal.Item;
import com.seekerscloud.Pos.modal.ItemDetails;
import com.seekerscloud.Pos.modal.Order;
import com.seekerscloud.Pos.view.tm.CartTm;
import com.seekerscloud.Pos.view.tm.ItemTm;
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
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

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
    public Label lblTotal;
    public JFXTextField txtOrderId;

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
        setOrderId();

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

    private void setOrderId() {
        if (Database.orderTable.isEmpty()){
            txtOrderId.setText("D-1");
            return;
        }
        String tempOrderId=Database.orderTable.get(Database.orderTable.size()-1).getOrderId();  //D-3
        String[] array=tempOrderId.split("-");  // [D,3]
        int tempNumber=Integer.parseInt(array[1]);
        int finalizeOrderId=tempNumber+1;
        txtOrderId.setText("D-"+finalizeOrderId);
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

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/Thogakade","root","1234");
            String sql="select code from Item";
            PreparedStatement statement=connection.prepareStatement(sql);
            ResultSet set=statement.executeQuery();

            ArrayList<String> codeList=new ArrayList<>();
            while (set.next()){
                codeList.add(set.getString(1));
            }
            ObservableList<String> obList=FXCollections.observableArrayList(codeList);
            cmbItemId.setItems(obList);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

       /* for (Item i : Database.itemTable) {
            cmbItemId.getItems().add(i.getCode());
        }*/
    }

    private void loadCustomerIds() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/Thogakade","root","1234");
            String sql="select id from Customer";
            PreparedStatement statement=connection.prepareStatement(sql);
            ResultSet set=statement.executeQuery();

            ArrayList<String> idList=new ArrayList<>();
            while (set.next()){
                idList.add(set.getString(1));

            }
            ObservableList<String> obList=FXCollections.observableArrayList(idList);
            cmbCustomerId.setItems(obList);
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

       /* for (Customer c : Database.customerTable) {
            cmbCustomerId.getItems().add(c.getId());
        }*/
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        placeOrderFormContext.getScene().getWindow();
        Stage stage = (Stage) placeOrderFormContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/DashboardForm.fxml"))));
    }
private boolean checkQty(String code,int qty){
    for (Item i:Database.itemTable
         ) {
       if (code.equals(i.getCode())){
           if (i.getQtyOnHand()>=qty){
               return true;
           }
           else {
               return false;
           }
       }
    }
    return false;
}
    ObservableList<CartTm> oblist = FXCollections.observableArrayList();

    public void addTocartOnAction(ActionEvent actionEvent) {

        if (!checkQty(cmbItemId.getValue(),Integer.parseInt(txtQuantity.getText()))){
            new Alert(Alert.AlertType.WARNING,"invalid qty").show();
            return;
        }

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

            if (!checkQty(cmbItemId.getValue(),tempQty)){
                new Alert(Alert.AlertType.WARNING,"invalid qty").show();
                return;
            }

            oblist.get(row).setQty(tempQty);
            oblist.get(row).setTotal(tempTotal);
            tblCart.refresh();
        }
        calculateTotal();
        clearFields();
        cmbItemId.requestFocus();

        btn.setOnAction(event -> {
            Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Are you sure",ButtonType.YES,ButtonType.NO);
            Optional<ButtonType> buttonType=alert.showAndWait();

            if (buttonType.get()==ButtonType.YES){
                for (CartTm tm:oblist
                     ) {
                   if (tm.getCode().equals(tm.getCode())){
                       oblist.remove(tm);
                       calculateTotal();
                       tblCart.refresh();
                       return;
                   }
                }
            }
        });

    }

    private void clearFields() {
        txtDescription.clear();
        txtUnitPrice.clear();
        txtQtyOnHand.clear();
        txtQuantity.clear();
    }


    private int isAlreadyExists(String code) {
        for (int i = 0; i < oblist.size(); i++) {
            if (oblist.get(i).getCode().equals(code)) {
                return i;
            }

        }
        return -1;
    }
    private void calculateTotal(){
        double total=0.00;
        for (CartTm tm:oblist
             ) {
            total+=tm.getTotal();
        }
        lblTotal.setText(String.valueOf(total));
    }

    public void placeOrderOnAction(ActionEvent actionEvent) {
        if (oblist.isEmpty()) return;
        ArrayList<ItemDetails> details=new ArrayList<>();
        for (CartTm tm:oblist
             ) {
            details.add(new ItemDetails(tm.getCode(),tm.getUnitPrice(),tm.getQty()));
        }
        Order order=new Order(txtOrderId.getText(),new Date(),Double.parseDouble(lblTotal.getText()),cmbCustomerId.getValue(),details );
        Database.orderTable.add(order);
        manageQty();
        clearAll();

    }

    private void manageQty() {
        for (CartTm tm:oblist
             ) {
            for (Item i:Database.itemTable
                 ) {
               if (i.getCode().equals(tm.getCode())){
                   i.setQtyOnHand(i.getQtyOnHand()-tm.getQty());
                   break;
               }
            }
        }
    }

    private void clearAll() {
        oblist.clear();
        calculateTotal();
        txtName.clear();
        txtAddress.clear();
        txtSalary.clear();
        clearFields();
        cmbCustomerId.requestFocus();
        setOrderId();
    }
}

