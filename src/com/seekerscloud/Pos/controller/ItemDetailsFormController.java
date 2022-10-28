package com.seekerscloud.Pos.controller;

import com.seekerscloud.Pos.db.DBConnection;
import com.seekerscloud.Pos.db.Database;
import com.seekerscloud.Pos.modal.ItemDetails;
import com.seekerscloud.Pos.modal.Order;
import com.seekerscloud.Pos.view.tm.ItemDetailsTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemDetailsFormController {
    public AnchorPane itemDetailsContext;
    public TableView<ItemDetailsTm> tblItems;
    public TableColumn colCode;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colTotal;

    public void initialize(){
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    public void loadOrderDEtails(String id){
        try {
            String sql="select o.orderId,d.itemCode,d.orderId,d.unitPrice,d.qty" + " from orders o inner join orders_details d where o.orderId=d.orderId and o.orderId=?" ;
            PreparedStatement statement= DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1,id);
            ResultSet set=statement.executeQuery();

            ObservableList<ItemDetailsTm> tmlist= FXCollections.observableArrayList();
            while (set.next()){
                double tempUnitPrice=set.getDouble(4);
                int tempQtyOnHand=set.getInt(5);
                double tempTotal=tempQtyOnHand*tempUnitPrice;
                tmlist.add(new ItemDetailsTm(
                       set.getString(2),tempUnitPrice,tempQtyOnHand,tempTotal
                ));
            }
            tblItems.setItems(tmlist);
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

        /*for (Order o: Database.orderTable
             ) {
            if (o.getOrderId().equals(id)){
                ObservableList<ItemDetailsTm> tmlist= FXCollections.observableArrayList();
                for (ItemDetails d:o.getItemDetails()
                     ) {
                    double tempUnitPrice=d.getUnitPrice();
                    int tempQtyOnHand=d.getQty();
                    double tempTotal=tempQtyOnHand*tempUnitPrice;
                    tmlist.add(new ItemDetailsTm(
                            d.getCode(),d.getUnitPrice(),d.getQty(),tempTotal
                    ));
                }
                tblItems.setItems(tmlist);
                return;
            }
        }*/
    }

    public void  backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        itemDetailsContext.getScene().getWindow();
        Stage stage=(Stage) itemDetailsContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/DashboardForm.fxml"))));
    }
}
