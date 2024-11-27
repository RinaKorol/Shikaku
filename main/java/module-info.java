module com.example.shikaku {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.shikaku to javafx.fxml;
    exports com.example.shikaku;
}