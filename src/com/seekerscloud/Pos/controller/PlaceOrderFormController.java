package com.seekerscloud.Pos.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.seekerscloud.Pos.db.DBConnection;
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

        try {
           String sql="select orderId from orders order by orderId desc limit 1";  //10 not working(unsigned)
           PreparedStatement statement= DBConnection.getInstance().getConnection().prepareStatement(sql);
           ResultSet set=statement.executeQuery();

           if (set.next()){
               String tempOrderId=set.getString(1);  //D-3
               String[] array=tempOrderId.split("-");  // [D,3]
               int tempNumber=Integer.parseInt(array[1]);
               int finalizeOrderId=tempNumber+1;
               txtOrderId.setText("D-"+finalizeOrderId);
           }
           else {
               txtOrderId.setText("D-1");
               return;
           }
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

       /* if (Database.orderTable.isEmpty()){
            txtOrderId.setText("D-1");
            return;
        }
        String tempOrderId=Database.orderTable.get(Database.orderTable.size()-1).getOrderId();  //D-3
        String[] array=tempOrderId.split("-");  // [D,3]
        int tempNumber=Integer.parseInt(array[1]);
        int finalizeOrderId=tempNumber+1;
        txtOrderId.setText("D-"+finalizeOrderId);*/
    }

    private void setItemDetails() {

        try {
            String sql="select * from Item where code=?";
            PreparedStatement statement=DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1,cmbItemId.getValue());
            ResultSet set=statement.executeQuery();

            if (set.next()) {
                txtDescription.setText(set.getString(2));
                txtUnitPrice.setText(String.valueOf(set.getDouble(3)));
                txtQtyOnHand.setText(String.valueOf(set.getInt(4)));

            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    private void setCustomerDetails() {

        try {
            String sql="select * from Customer where id=?";
            PreparedStatement statement=DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1,cmbCustomerId.getValue());
            ResultSet set=statement.executeQuery();
            if (set.next()){
               txtName.setText(set.getString(2));
               txtAddress.setText(set.getString(3));
               txtSalary.setText(String.valueOf(set.getInt(4)));
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


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
            String sql="select code from Item";
            PreparedStatement statement=DBConnection.getInstance().getConnection().prepareStatement(sql);
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
            String sql="select id from Customer";
            PreparedStatement statement=DBConnection.getInstance().getConnection().prepareStatement(sql);
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

        try{
            String sql="select qtyOnHand from Item where code=?";
            PreparedStatement statement=DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1,code);
            ResultSet set=statement.executeQuery();

            if (set.next()){
                //check
                int tempQty=set.getInt(1);
                if (tempQty>=qty){
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

   /* for (Item i:Database.itemTable
         ) {
       if (code.equals(i.getCode())){
           if (i.getQtyOnHand()>=qty){
               return true;
           }
           else {
               return false;
           }
       }
    }*/
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

        try {
            String sql="insert into orders values(?,?,?,?)";
            PreparedStatement statement=DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1,order.getOrderId());
            statement.setString(2,txtDate.getText());
            statement.setDouble(3,order.getTotalCost());
            statement.setString(4,order.getCustomer());

            boolean isOrdersSaved=statement.executeUpdate()>0;
            if (isOrdersSaved){

                //update qty,manage qty
               boolean isAllUpdated= manageQty(details);
               if (isAllUpdated){
                   new Alert(Alert.AlertType.CONFIRMATION,"Order Placed").show();
                   clearAll();
               }
               else {
                   new Alert(Alert.AlertType.WARNING,"Try Again").show();
               }
            }
            else {
                new Alert(Alert.AlertType.WARNING,"Try Again !").show();
            }

        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

       /* Database.orderTable.add(order);
        manageQty();
        clearAll();*/

    }

    private boolean manageQty(ArrayList<ItemDetails> details) {

        try {
            for (ItemDetails d:details
                 ) {
                String sql="insert into orders_details values (?,?,?,?)";
                PreparedStatement statement=DBConnection.getInstance().getConnection().prepareStatement(sql);
                statement.setString(1,d.getCode());
                statement.setString(2,txtOrderId.getText());
                statement.setDouble(3,d.getUnitPrice());
                statement.setInt(4,d.getQty());

                boolean isOrderDetailsSaved=statement.executeUpdate()>0;
                if (isOrderDetailsSaved){
                    boolean isQtyUpdated=update(d);
                    if (!isQtyUpdated){
                        return false;
                    }
                }
                else {
                    return false;
                }


            }
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

        for (ItemDetails d:details
             ) {
           boolean isQtyUpdated=update(d);
           if (!isQtyUpdated){
               return false;
           }
        }
return true;
        /*for (CartTm tm:oblist
             ) {
            for (Item i:Database.itemTable
                 ) {
               if (i.getCode().equals(tm.getCode())){
                   i.setQtyOnHand(i.getQtyOnHand()-tm.getQty());
                   break;
               }
            }
        }*/
    }

    private boolean update(ItemDetails d) {
        try {
            String sql="update Item set qtyOnHand=qtyOnHand-? where code=?";
            PreparedStatement statement=DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setInt(1,d.getQty());
            statement.setString(2,d.getCode());
            return statement.executeUpdate()>0;
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            return false;
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

