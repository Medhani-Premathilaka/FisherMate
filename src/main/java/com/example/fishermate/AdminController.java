package com.example.fishermate;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminController {

    @FXML
    private Button addnewuser;

    @FXML
    private AnchorPane adminpane;

    @FXML
    private Button boatrides;

    @FXML
    private Button btnlogout;

    @FXML
    private Button history;






        @FXML
        private Button btnclose;

        @FXML
        private Button btnregister;

        @FXML
        private PasswordField confirmpassword;

        @FXML
        private Label lblconfirmpassword;

        @FXML
        private Label lblfname;

        @FXML
        private Label lbllname;

        @FXML
        private Label lblpassword;

        @FXML
        private Label lbluname;

        @FXML
        private PasswordField password1;

        @FXML
        private AnchorPane registerform;

        @FXML
        private TextField txtfname;

        @FXML
        private TextField txtlname;

        @FXML
        private TextField txtuname;

        @FXML
        private  Label lblmismatch;


//    public void initialize(URL url, ResourceBundle resourceBundle){
//
//    }resourceBundle


        public void close(ActionEvent event){
            Stage stage = (Stage) btnclose.getScene().getWindow();
            stage.close();
        }

        public void onClickRegister(){
            if(password1.getText().equals(confirmpassword.getText())){
                System.out.println("Password Matched");
                registerUser();

                // Show the Alert

            }else {
                lblmismatch.setText("Password Mismatch");
                //System.out.println("Password Mismatch");
            }
        }

        public  void registerUser(){
            DBconnection conn = new DBconnection();
            Connection connectDB = conn.getConnection();

            String firstname = txtfname.getText();
            String lastname = txtlname.getText();
            String username = txtuname.getText();
            String password = password1.getText();

            String insertFields = "INSERT INTO login (firstname, lastname, username, password) VALUES ('"+firstname+"','"+lastname+"','"+username+"','"+password+"')";

            try{
                Statement stmt = connectDB.createStatement();
                stmt.executeUpdate(insertFields);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registration Successful");
                alert.setHeaderText(null);
                alert.setContentText("Your registration was successful!");
                alert.showAndWait();
                Platform.exit();//remove stage after successful registration
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }


    public void addNew(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Register.fxml"));
            Parent root = fxmlLoader.load(); // Load the FXML and get the Parent object
            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.UNDECORATED);
            registerStage.setScene(new Scene(root, 520, 568)); // Pass the Parent object to the Scene
            registerStage.setTitle("Registration Page");
            registerStage.setResizable(false);
            registerStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    public void history(){
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
//    public void boatrides(){
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
    public void logout(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to logout?");
        alert.showAndWait();
        onclicklogout();
    }

    public void onclicklogout(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Login.fxml"));
            Parent root = fxmlLoader.load(); // Load the FXML and get the Parent object
            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.UNDECORATED);
            registerStage.setScene(new Scene(root, 600, 400)); // Pass the Parent object to the Scene
            registerStage.setTitle("Registration Page");
            registerStage.setResizable(false);
            registerStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
