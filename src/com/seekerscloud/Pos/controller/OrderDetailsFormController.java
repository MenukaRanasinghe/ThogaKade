package com.seekerscloud.Pos.controller;

import com.seekerscloud.Pos.db.Database;
import com.seekerscloud.Pos.modal.Order;
import com.seekerscloud.Pos.view.tm.OrderTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class OrderDetailsFormController {
    public AnchorPane orderDetailsContext;
    public TableView<OrderTm> tblOrders;
    public TableColumn colID;
    public TableColumn colName;
    public TableColumn colDate;
    public TableColumn colTotal;
    public TableColumn colOption;

    public void initialize(){
        colID.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        loadOrders();
    }

    private void loadOrders() {
        ObservableList<OrderTm> tmlist= FXCollections.observableArrayList();
        for (Order o: Database.orderTable
             ) {
            Button btn=new Button("view more");
            OrderTm tm=new OrderTm(o.getOrderId(),o.getCustomer(),o.getDate(),o.getTotalCost(),btn);
            tmlist.add(tm);

            btn.setOnAction(e->{

                try {
                    FXMLLoader loader=new FXMLLoader(getClass().getResource("../view/ItemDetailsForm.fxml"));
                    Parent parent=loader.load();
                    ItemDetailsFormController controller=loader.getController();
                    controller.loadOrderDEtails(tm.getOrderId());
                    Stage stage=new Stage();
                    stage.setScene(new Scene(parent));
                    stage.show();

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } );
        }
        tblOrders.setItems(tmlist);
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        orderDetailsContext.getScene().getWindow();
        Stage stage=(Stage) orderDetailsContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/DashboardForm.fxml"))));
    }
}
