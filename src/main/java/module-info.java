module eus.ehu {
    requires javafx.controls;
    requires javafx.fxml;

    opens eus.ehu to javafx.fxml;
    exports eus.ehu;
}
