package eus.ehu.ui;

import eus.ehu.domain.MotorType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CreateController {

    @FXML
    private TextField brand;

    @FXML
    private TextField age;

    @FXML
    private TextArea description;

    @FXML
    private TextField horsePower;

    @FXML
    private TextField mileage;

    @FXML
    private TextField model;

    @FXML
    private ComboBox<MotorType> motorType;

    @FXML
    private Button postButton;

    @FXML
    private TextField price;

    @FXML
    void initialize() { 
        motorType.getItems().addAll(MotorType.values());
    }

    @FXML
    void post() {
        String brand = this.brand.getText();
        if (brand==null || brand.isEmpty()) {
            // Show error message
            return;
        }
        String model = this.model.getText();
        int age = Integer.parseInt(this.age.getText());
        int mileage = Integer.parseInt(this.mileage.getText());
        int horsePower = Integer.parseInt(this.horsePower.getText());
        MotorType motorType = this.motorType.getValue();
        String description = this.description.getText();

    }

}
