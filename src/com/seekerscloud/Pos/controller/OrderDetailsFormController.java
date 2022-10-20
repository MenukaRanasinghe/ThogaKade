package com.seekerscloud.Pos.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class OrderDetailsFormController {
    public AnchorPane orderDetailsContext;
    public TableView tblOrders;
    public TableColumn colID;
    public TableColumn colName;
    public TableColumn colDate;
    public TableColumn colTotal;
    public TableColumn colOption;

    public void backToHomeOnAction(ActionEvent actionEvent) {
    }
}
