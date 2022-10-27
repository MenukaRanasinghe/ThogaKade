package com.seekerscloud.Pos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.seekerscloud.Pos.db.DBConnection;
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
import java.sql.*;
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

    public void initialize() throws SQLException, ClassNotFoundException {
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
            try {
                searchCustomers(searchText);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
    private void setData(CustomerTm tm){
        txtID.setText(tm.getId());
        txtName.setText(tm.getName());
        txtAddress.setText(tm.getAddress());
        txtSalary.setText(String.valueOf(tm.getSalary()));
        btnSaveCustomer.setText("Update Customer");
    }
    private void searchCustomers(String text) throws ClassNotFoundException, SQLException {

        String searchText="%"+text+"%";


        ObservableList<CustomerTm> tmList= FXCollections.observableArrayList();
        String sql="select * from Customer where name like ? || address like ? ";
        PreparedStatement statement= DBConnection.getInstance().getConnection().prepareStatement(sql);
        statement.setString(1,searchText);
        statement.setString(2,searchText);
        ResultSet set=statement.executeQuery();

        while (set.next()){
                Button btn=new Button("Delete");
                CustomerTm tm=new CustomerTm(set.getString(1),set.getString(2),set.getString(3),set.getDouble(4),btn);
                tmList.add(tm);

                btn.setOnAction(event -> {
                    Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Are You Sure?", ButtonType.YES,ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get()==ButtonType.YES){

                        try {
                            String sql1="delete from Customer where id=?";
                            PreparedStatement statement1=DBConnection.getInstance().getConnection().prepareStatement(sql1);
                            statement1.setString(1,tm.getId());

                            if (statement1.executeUpdate()>0){
                                try {
                                    searchCustomers(searchText);
                                } catch (ClassNotFoundException e) {
                                    throw new RuntimeException(e);
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                                clearFields();
                                new Alert(Alert.AlertType.INFORMATION,"Customer Deleted !").show();
                            }
                            else {
                                new Alert(Alert.AlertType.WARNING,"Try Again !").show();
                            }

                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }


                      //  boolean isDeleted=Database.customerTable.remove(c);

                    }

                });
        }
            tblCustomer.setItems(tmList);

      /*  ObservableList<CustomerTm> tmList= FXCollections.observableArrayList();
        for (Customer c:Database.customerTable
             ) {


        }*/

    }

    public void saveCustomerOnAction(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        Customer c1=new Customer(txtID.getText(),txtName.getText(),txtAddress.getText(),Double.parseDouble(txtSalary.getText()));

        if (btnSaveCustomer.getText().equalsIgnoreCase("save customer")){

            //database
            //step1---driver load to ram
            //Class.forName("com.mysql.cj.jdbc.Driver");
            //step2---create connection
           // Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/Thogakade","root","1234");
            String sql="insert into Customer values(?,?,?,?)";
            PreparedStatement statement=DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1,c1.getId());
            statement.setString(2,c1.getName());
            statement.setString(3,c1.getAddress());
            statement.setDouble(4,c1.getSalary());
            //step3---create statement
           // Statement statement=connection.createStatement();
            //step4---create query
           // String sql="insert into Customer values('"+c1.getId()+"','"+c1.getName()+"','"+c1.getAddress()+"','"+c1.getSalary()+"')";
            //step5---statement execute
          //  int isSaved= statement.executeUpdate(sql);

            if (statement.executeUpdate()>0){
                searchCustomers(searchText);
                clearFields();
                new Alert(Alert.AlertType.INFORMATION,"Customer Saved !").show();
            }
            else {
                new Alert(Alert.AlertType.WARNING,"Try Again !").show();
            }
        }
        else {
            String sql="update Customer set name=?,address=?,salary=? where id=?";
            PreparedStatement statement=DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1,c1.getName());
            statement.setString(2,c1.getAddress());
            statement.setDouble(3,c1.getSalary());
            statement.setString(4,c1.getId());
            if (statement.executeUpdate()>0){
                searchCustomers(searchText);
                clearFields();
                new Alert(Alert.AlertType.INFORMATION,"Customer Updated !").show();
            }
            else {
                new Alert(Alert.AlertType.WARNING,"Try Again !").show();
            }
            /*for (int i = 0; i < Database.customerTable.size(); i++) {
                if (txtID.getText().equalsIgnoreCase(Database.customerTable.get(i).getId())){
                    Database.customerTable.get(i).setName(txtName.getText());
                    Database.customerTable.get(i).setAddress(txtAddress.getText());
                    Database.customerTable.get(i).setSalary(Double.parseDouble(txtSalary.getText()));
                    searchCustomers(searchText);
                    new Alert(Alert.AlertType.INFORMATION,"Customer Updated").show();
                    clearFields();
                }
            }*/
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
