module com.example.fishermate {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.fishermate to javafx.fxml;
    exports com.example.fishermate;
}