package com.example.fishermate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class LoginController  {

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
        if (!username.getText().isEmpty() && !password.getText().isEmpty()) {

            validateLogin(event);

        } else {
            txterror.setText("Please enter username and password");
        }
    }

    public void adminpg(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Admin.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void userpg(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("User.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

//    public void validateLogin(ActionEvent event) {
//        DBconnection connectNow = new DBconnection();
//        Connection connectDB = connectNow.getConnection();
//
//        String verifyLogin = "SELECT COUNT(1) FROM login WHERE username = ? AND password = ?";
//
//        try {
//            PreparedStatement pstmt = connectDB.prepareStatement(verifyLogin);
//            pstmt.setString(1, username.getText());
//            pstmt.setString(2, password.getText());
//            ResultSet rs = pstmt.executeQuery();
//
//            if (rs.next() && rs.getInt(1) == 1) {
//                String u1 = username.getText();
//                String p1 = password.getText();
//                getData.username = u1;
//                if (u1.equals("admin") && p1.equals("admin")) {
//                    adminpg(event);
//
//                } else {
//                    userpg(event);
//                    getData.username = username.getText();
//                }
//            } else {
//                txterror.setText("Invalid login. Please try again.");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
public void validateLogin(ActionEvent event) {
    DBconnection connectNow = new DBconnection();
    Connection connectDB = connectNow.getConnection();

    String verifyLogin = "SELECT COUNT(1) FROM login WHERE username = ? AND password = ?";

    try {
        Encryptor encryptor = new Encryptor();
        String encryptedPassword = encryptor.encryptString(password.getText());

        PreparedStatement pstmt = connectDB.prepareStatement(verifyLogin);
        pstmt.setString(1, username.getText());
        pstmt.setString(2, encryptedPassword);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next() && rs.getInt(1) == 1) {
            String u1 = username.getText();
            getData.username = u1;
            if (u1.equals("admin")) {
                adminpg(event);
            } else {
                userpg(event);
            }
        } else {
            txterror.setText("Invalid login. Please try again.");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public void closeApp() {
        System.exit(0);
    }

    public void minimizeApp() {
        Stage stage = (Stage) btnlogin.getScene().getWindow();
        stage.setIconified(true);
    }




}