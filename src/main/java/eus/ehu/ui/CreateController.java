package eus.ehu.ui;

import eus.ehu.businesslogic.BlManager;
import eus.ehu.domain.ListingStatus;
import eus.ehu.domain.MotorType;
import eus.ehu.domain.Sale;
import eus.ehu.domain.Vehicle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateController {

    BlManager bl;

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
    private TextField licensePlate;

    @FXML
    private ComboBox<MotorType> motorType;

    @FXML
    private Button postButton;

    @FXML
    private TextField price;

    @FXML
    private Button profileName;

    @FXML
    private void handleGoToSearch() {
        try {
            App.setRoot("FilterSearch");
        } catch (Exception e) {
            throw new RuntimeException("Unable to open Filter Search scene", e);
        }
    }

    @FXML
    private void handleViewProfile() {
        try {
            App.setRoot("Profile");
        } catch (Exception e) {
            throw new RuntimeException("Unable to open Profile scene", e);
        }
    }

    @FXML
    void initialize() {
        bl = BlManager.getInstance();

        motorType.getItems().addAll(MotorType.values());
        profileName.setText("Welcome, " + App.getActualUser().getName() + "!");
    }

    @FXML
    void post() {
        boolean done = false;
        try {
            String licensePlate = requiredText(this.licensePlate, "License plate is required");
            if (bl.getVehicleByLicensePlate(licensePlate) != null) {
                throw new IllegalStateException("There is already a vehicle with that license plate");
            }
            String brand = requiredText(this.brand, "Brand is required");
            String model = requiredText(this.model, "Model is required");
            int age = parseRequiredPositiveInt(this.age, "Age must be a valid number");
            int mileage = parseRequiredPositiveInt(this.mileage, "Mileage must be a valid number");
            int horsePower = parseRequiredPositiveInt(this.horsePower, "Horsepower must be a valid number");
            double price = parseRequiredPositiveDouble(this.price, "Price must be a valid number");
            MotorType motorType = this.motorType.getValue();
            if (motorType == null) {
                throw new NullPointerException("You must select a motor type");
            }
            String description = this.description.getText();

            eus.ehu.domain.User user = bl.getDefaultUser();
            if (user == null) {
                throw new NullPointerException("Default user not available");
            }

            Sale sale = new Sale.Builder()
                    .description(description)
                    .status(ListingStatus.ACTIVE)
                    .user(user)
                    .fixedPrice(price)
                    .build();
            Vehicle v = new Vehicle.Builder(licensePlate)
                    .make(brand)
                    .model(model)
                    .year(age)
                    .mileage(mileage)
                    .horsepower(horsePower)
                    .motorType(motorType)
                    .listing(sale)
                    .build();
            bl.storeSale(sale);
            bl.storeVehicle(v);
            bl.addVehicleToSale(sale, v);
            bl.addSaleToUser(user, sale);

            System.out.println("OK -> " + licensePlate + " " + brand + " " + model + " " + age + " " + mileage + " "
                    + horsePower + " " + price + " " + motorType + " " + description);
            done = true;
        } catch (NullPointerException | IllegalStateException | IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
        if (done) {
            try {
                App.setRoot("FilterSearch");
            } catch (Exception e) {
                throw new RuntimeException("Unable to open Filter Search scene", e);
            }
            // bl.close();
            // closeWindow();
        }
    }

    private String requiredText(TextField field, String message) {
        if (field == null) {
            throw new NullPointerException("Field is not initialized (FXML)");
        }
        String value = field.getText();
        if (value == null || value.isBlank()) {
            throw new NullPointerException(message);
        }
        return value.trim();
    }

    private int parseRequiredInt(TextField field, String message) {
        String value = requiredText(field, message);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new NumberFormatException(message);
        }
    }

    private int parseRequiredPositiveInt(TextField field, String message) {
        int value = parseRequiredInt(field, message);
        if (value <= 0) {
            throw new IllegalArgumentException(message + " (must be greater than 0)");
        }
        return value;
    }

    private double parseRequiredDouble(TextField field, String message) {
        String value = requiredText(field, message);
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            throw new NumberFormatException(message);
        }
    }

    private double parseRequiredPositiveDouble(TextField field, String message) {
        double value = parseRequiredDouble(field, message);
        if (value <= 0) {
            throw new IllegalArgumentException(message + " (must be greater than 0)");
        }
        return value;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Cannot publish listing");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        ((Stage) postButton.getScene().getWindow()).close();
    }

}
