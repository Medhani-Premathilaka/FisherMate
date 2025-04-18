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

    public void userLoging(ActionEvent event) {

        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            txterror.setText("Please enter username and password");

        }else {
            try {
                gotopage1(event); // Pass the ActionEvent to the method
            } catch (IOException e) {
                e.printStackTrace();
            }
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
}