package com.example.fishermate;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class RegisterController  {

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
            //System.out.println("Password Matched");
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
}
