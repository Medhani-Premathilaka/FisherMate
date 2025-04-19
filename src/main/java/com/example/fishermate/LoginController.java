package com.example.fishermate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginController {

    @FXML
    private Button btnlogin;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;


    @FXML
    private Label txterror;
    private Stage stage;
    private Scene scene;
    ActionEvent event;
    public void userLoging(ActionEvent event) {

        if (!username.getText().isEmpty() && !password.getText().isEmpty()) {
            //txterror.setText("Please enter username and password");
            validateLogin(event);

        }else {
            txterror.setText("Please enter username and password");
        }


    }
    public void gotopage1(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("InputDetails.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        System.out.println("Welcome to Page 2!");


    }
    public void validateLogin(ActionEvent event){
        DBconnection connectNow = new DBconnection();
        Connection connectDB = connectNow.getConnection();

//        if (connectDB == null) {
//            txterror.setText("Database connection failed. Please try again later.");
//            return;
//        }

        String verifyLogin = "SELECT COUNT(1) FROM login WHERE username = '" + username.getText() + "' AND password = '" + password.getText() + "'";

        try{
            Statement stmt = connectDB.createStatement();
            ResultSet rs = stmt.executeQuery(verifyLogin);

            while (rs.next()){
                if (rs.getInt(1) == 1){
                    //txterror.setText("Welcome to FisherMate");

                    gotopage1(event);
                }else {
                    txterror.setText("Invalid login. Please try again.");
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }


}