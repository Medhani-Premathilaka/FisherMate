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
import javafx.stage.StageStyle;

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
    public void adminpg(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("Admin.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        System.out.println("Welcome to Page 2!");


    }
    public void userpg(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("User.fxml"));
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
                    String u1 = username.getText();
                    String p1 = password.getText();
                    if(u1.equals("admin") && p1.equals("admin")) {
                        //txterror.setText("Welcome to FisherMate");
                        adminpg(event);
                    }else {
                        userpg(event);
                    }


                }else {
                    txterror.setText("Invalid login. Please try again.");
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

//    public void createAccout() {
//        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Register.fxml"));
//            Parent root = fxmlLoader.load(); // Load the FXML and get the Parent object
//            Stage registerStage = new Stage();
//            registerStage.initStyle(StageStyle.UNDECORATED);
//            registerStage.setScene(new Scene(root, 520, 568)); // Pass the Parent object to the Scene
//            registerStage.setTitle("Registration Page");
//            registerStage.setResizable(false);
//            registerStage.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public void closeApp() {
        System.exit(0);
    }

    public void minimizeApp() {
        Stage stage = (Stage) btnlogin.getScene().getWindow();
        stage.setIconified(true);
    }


}