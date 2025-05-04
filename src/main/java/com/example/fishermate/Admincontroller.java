package com.example.fishermate;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.List;


public class Admincontroller implements Initializable {

    @FXML
    private Button addnewuser;

    @FXML
    private AnchorPane adminpane;

    @FXML
    private Button boatrides;

    @FXML
    private Button btnclose;

    @FXML
    private Button btnlogout;

    @FXML
    private Button btnregister;

    @FXML
    private Button btnregister1;

    @FXML
    private PasswordField confirmpassword;

    @FXML
    private Button history;

    @FXML
    private AnchorPane historypane;

    @FXML
    private Label lblconfirmpassword;

    @FXML
    private Label lblfname;

    @FXML
    private Label lbllname;

    @FXML
    private Label lblmismatch;

    @FXML
    private Label lblpassword;

    @FXML
    private Label lbluname;

    @FXML
    private ImageView newUserImage;

    @FXML
    private PasswordField password1;

    @FXML
    private AnchorPane registerform;

    @FXML
    private TextField ride_crew;

    @FXML
    private DatePicker ride_date;

    @FXML
    private ComboBox<String> ride_days;

    @FXML
    private ComboBox<String > ride_location;

    @FXML
    private Label ride_notice;

    @FXML
    private Button ride_submit;

    @FXML
    private AnchorPane ridespane;

    @FXML
    private TextField txtfname;

    @FXML
    private TextField txtlname;

    @FXML
    private TextField txtuname;

    @FXML
    private Label username;

    //    public void initialize(URL url, ResourceBundle resourceBundle){
//
//    }resourceBundle
    Encryptor encryptor = new Encryptor();
    private Image image;
    private PreparedStatement PreparedStatement;

    private String[] listLocation = {"Galle", "Matara", "Hambantota", "Trincomalee", "Jaffna",
            "Negombo", "Colombo", "Batticaloa", "Kalpitiya"};
    public void addLocation(){
        List<String> listG = new ArrayList<>();

        for(String data: listLocation){
            listG.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(listG);
        ride_location.setItems(listData);
    }

    private String[] listDays = {"1","2","3","4","5","6","7","8","9","10"};
    public void addDays(){
        List<String> listG = new ArrayList<>();

        for(String data: listDays){
            listG.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(listG);
        ride_days.setItems(listData);
    }

    final static int FORECASTQTY = 8;

    public static int realMaxDays(Inputs inputdata) {
        int numberOfForecasts = inputdata.getMaxDays() * FORECASTQTY;
        WeatherData data = null;
        for (int i = 0; i < numberOfForecasts; i++) {
            data = DBconnection.dataRetriever(inputdata);
            if (compare(data) == -1) {
                return i / FORECASTQTY;
            }
            inputdata.setDateTime(updateTime(inputdata));
        }

        return inputdata.getMaxDays();
    }
    public static int compare(WeatherData data) {

        if (data.getRainProbability() > 70 || data.getWindSpeed() > 50 || data.isStorms()
                || data.getVisibility() < 1000
                || data.isLightning()) {
            return -1;

        } else {
            return 1;
        }
    }

    public static String updateTime(Inputs inputData) {

        String updatedTime = null;

        Timestamp timestamp = Timestamp.valueOf(inputData.getDateTime());

        // Create Calendar to add 3 hours
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        cal.add(Calendar.HOUR_OF_DAY, 3);

        // Get new Timestamp
        Timestamp newTimestamp = new Timestamp(cal.getTimeInMillis());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Convert to LocalDateTime and format it
        updatedTime = newTimestamp.toLocalDateTime().format(formatter);

        return updatedTime;
    }

    public static void Consumption(Inputs inputData, int days) {
        System.out.println("Approximating the consumptions...");
        double water = inputData.getNoOfCrewMembers() * 25 * days;
        System.out.println("Maximum water Consumption: " + water + " liters");

        double food = inputData.getNoOfCrewMembers() * 2.5 * days;
        System.out.println("Maximum food Consumption: " + food + " kg");

        double fuel = 500 * days;
        System.out.println("Maximum fuel Consumption: " + fuel + " liters");

    }

    public  void finalDecision(Inputs inputdata) {
        WeatherData data = DBconnection.dataRetriever(inputdata);
        System.out.println();
        if (compare(data) == -1) {
            System.out.println("The trip is not recommended due to bad weather conditions.");
        } else {
            System.out.println("The trip is recommended based on the weather conditions.");
            if (realMaxDays(inputdata) == 0) {
                System.out.println(
                        "Unfortunately based on the weather condition of the rest of the day, you cannot stay for the whole day.");
            } else if (realMaxDays(inputdata) < inputdata.getMaxDays()) {
                System.out
                        .println("You can only stay safely for only " + realMaxDays(inputdata) + " days.");
                Consumption(inputdata, realMaxDays(inputdata));
            } else {
                System.out.println("You can stay your maximumly expected  " + inputdata.getMaxDays() + " days.");
                Consumption(inputdata, inputdata.getMaxDays());
            }

        }
    }

    public void close(ActionEvent event){
            Stage stage = (Stage) btnclose.getScene().getWindow();
            stage.close();
        }

        public void onClickRegister() throws NoSuchAlgorithmException {
            if(password1.getText().equals(confirmpassword.getText())){
                System.out.println("Password Matched");
                registerUser();

                // Show the Alert

            }else {
                lblmismatch.setText("Password Mismatch");
                //System.out.println("Password Mismatch");
            }
        }

//        public  void registerUser(){
//            DBconnection conn = new DBconnection();
//            Connection connectDB = conn.getConnection();
//
//            String firstname = txtfname.getText();
//            String lastname = txtlname.getText();
//            String username = txtuname.getText();
//            String password = password1.getText();
//            String uri = getData.path;
//            uri = uri.replace("\\", "\\\\");
//            String image = newUserImage.getText();
//
//            String insertFields = "INSERT INTO login (firstname, lastname, username, password,image) VALUES ('"+firstname+"','"+lastname+"','"+username+"','"+password+",'"+image+"'')";
//
//            try{
//                Statement stmt = connectDB.createStatement();
//                stmt.executeUpdate(insertFields);
//                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setTitle("Registration Successful");
//                alert.setHeaderText(null);
//                alert.setContentText("Your registration was successful!");
//                alert.showAndWait();
//                Platform.exit();//remove stage after successful registration
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//
//        }

    public void reset(){
        txtfname.setText("");
        txtlname.setText("");
        txtuname.setText("");
        password1.setText("");
        confirmpassword.setText("");
        newUserImage.setImage(null);

    }
public void registerUser() throws NoSuchAlgorithmException {
    DBconnection conn = new DBconnection();
    Connection connectDB = conn.getConnection();

    String firstname = txtfname.getText();
    String lastname = txtlname.getText();
    String username = txtuname.getText();
    String password2 = password1.getText();
    String password = encryptor.encryptString(password2);
    String uri = getData.path.replace("\\", "\\\\"); // Escape backslashes for SQL

    String insertFields = "INSERT INTO login (firstname, lastname, username, password, image) VALUES (?, ?, ?, ?, ?)";

    try {
        PreparedStatement pstmt = connectDB.prepareStatement(insertFields);
        pstmt.setString(1, firstname);
        pstmt.setString(2, lastname);
        pstmt.setString(3, username);
        pstmt.setString(4, password);
        pstmt.setString(5, uri);

        pstmt.executeUpdate();

        reset();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration Successful");
        alert.setHeaderText(null);
        alert.setContentText("Your registration was successful!");
        alert.showAndWait();

        //Platform.exit(); // Close the application after successful registration
    } catch (SQLException e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Registration Failed");
        alert.setHeaderText(null);
        alert.setContentText("An error occurred during registration. Please try again.");
        alert.showAndWait();
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

public void logout(ActionEvent event) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Logout");
    alert.setHeaderText(null);
    alert.setContentText("Do you want to logout?");

    if (alert.showAndWait().get() == ButtonType.OK) {
        onclicklogout(); // Redirect to the login page
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close(); // Close the current admin page
    } else  {
        System.out.println("Logout cancelled");

    }
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

    public void importImage(){
        FileChooser open = new FileChooser();
        File file = open.showOpenDialog(addnewuser.getScene().getWindow());

        if(file != null){
            getData.path = file.getAbsolutePath();

            image = new Image(file.toURI().toString(),139, 125, false, true );
            newUserImage.setImage(image);
        }
    }

//    public void displaUsername() {
//            String sql = "select username from login where username = ?";
//            DBconnection conn = new DBconnection();
//        System.out.println("connected");
//        try{
//            Connection connectDB = conn.getConnection();
//            PreparedStatement pstmt = connectDB.prepareStatement(sql);
//            pstmt.setString(1, username.getText());
//            ResultSet rs = pstmt.executeQuery();
//
//            if (rs.next()) {
//                getData.username = rs.getString("username");
//                username.setText(getData.username);
//            } else {
//                System.out.println("No username found");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }
public void displaUsername() {
    if (getData.username != null && !getData.username.isEmpty()) {
        System.out.println("Username found: " + getData.username); // Debug log
        username.setText(getData.username); // Display the username
    } else {
        System.out.println("No username found in getData");
    }
}
    public void switchform(ActionEvent event){

            if(event.getSource()== addnewuser){
                registerform.setVisible(true);
                historypane.setVisible(false);
                ridespane.setVisible(false);

            } else if (event.getSource() == history) {
                registerform.setVisible(false);
                historypane.setVisible(true);
                ridespane.setVisible(false);

            }else if (event.getSource() == boatrides) {
                registerform.setVisible(false);
                historypane.setVisible(false);
                ridespane.setVisible(true);

            }
    }


    @Override
    public void initialize(java.net.URL url, java.util.ResourceBundle resourceBundle) {
        displaUsername();
    }
}
