module com.example.fishermate {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.fishermate to javafx.fxml;
    exports com.example.fishermate;
}