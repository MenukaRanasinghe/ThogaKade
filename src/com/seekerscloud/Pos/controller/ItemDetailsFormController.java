package com.seekerscloud.Pos.controller;

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
        for (Order o: Database.orderTable
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
        }
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        itemDetailsContext.getScene().getWindow();
        Stage stage=(Stage) itemDetailsContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/DashboardForm.fxml"))));
    }
}
