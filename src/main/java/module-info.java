module com.example.fishermate {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.json;


    opens com.example.fishermate to javafx.fxml;
    exports com.example.fishermate;
}