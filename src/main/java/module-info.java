module app.aikidopracticetracker {
    requires javafx.controls;
    requires javafx.fxml;


    opens app.aikidopracticetracker to javafx.fxml;
    exports app.aikidopracticetracker;
}