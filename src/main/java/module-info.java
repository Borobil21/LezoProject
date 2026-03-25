module eus.ehu {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    opens eus.ehu to javafx.fxml;
    exports eus.ehu.ui;
}