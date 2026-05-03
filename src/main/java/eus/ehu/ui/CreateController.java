package eus.ehu.ui;

import java.util.ResourceBundle;

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
import javafx.util.StringConverter;

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
        motorType.setConverter(new StringConverter<>() {
            @Override
            public String toString(MotorType motorType) {
                return getMotorTypeText(motorType);
            }

            @Override
            public MotorType fromString(String string) {
                return null;
            }
        });
        profileName.setText("Welcome, " + App.getActualUser().getName() + "!");
    }

    @FXML
    void post() {
        boolean done = false;
        try {
            String licensePlate = requiredText(this.licensePlate, tr("error.create.license_required", "License plate is required"));
            if (bl.getVehicleByLicensePlate(licensePlate) != null) {
                throw new IllegalStateException(tr("error.create.license_exists", "There is already a vehicle with that license plate"));
            }
            String brand = requiredText(this.brand, tr("error.create.brand_required", "Brand is required"));
            String model = requiredText(this.model, tr("error.create.model_required", "Model is required"));
            int age = parseRequiredPositiveInt(
                    this.age,
                    tr("error.create.age_invalid", "Age must be a valid number"),
                    tr("error.create.age_positive", "Age must be greater than 0"));
            int mileage = parseRequiredPositiveInt(
                    this.mileage,
                    tr("error.create.mileage_invalid", "Mileage must be a valid number"),
                    tr("error.create.mileage_positive", "Mileage must be greater than 0"));
            int horsePower = parseRequiredPositiveInt(
                    this.horsePower,
                    tr("error.create.horsepower_invalid", "Horsepower must be a valid number"),
                    tr("error.create.horsepower_positive", "Horsepower must be greater than 0"));
            double price = parseRequiredPositiveDouble(
                    this.price,
                    tr("error.create.price_invalid", "Price must be a valid number"),
                    tr("error.create.price_positive", "Price must be greater than 0"));
            MotorType motorType = this.motorType.getValue();
            if (motorType == null) {
                throw new NullPointerException(tr("error.create.motor_required", "You must select a motor type"));
            }
            String description = this.description.getText();

            // Get the actual user (currently logged in), not the default user
            eus.ehu.domain.User user = App.getActualUser();
            if (user == null) {
                throw new NullPointerException(tr("error.create.user_unavailable", "User not available"));
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

    private double parseRequiredDouble(TextField field, String message) {
        String value = requiredText(field, message);
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            throw new NumberFormatException(message);
        }
    }

    private int parseRequiredPositiveInt(TextField field, String invalidMessage, String positiveMessage) {
        int value = parseRequiredInt(field, invalidMessage);
        if (value <= 0) {
            throw new IllegalArgumentException(positiveMessage);
        }
        return value;
    }

    private double parseRequiredPositiveDouble(TextField field, String invalidMessage, String positiveMessage) {
        double value = parseRequiredDouble(field, invalidMessage);
        if (value <= 0) {
            throw new IllegalArgumentException(positiveMessage);
        }
        return value;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(tr("alert.error_title", "Error"));
        alert.setHeaderText(tr("error.create.cannot_publish", "Cannot publish listing"));
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String tr(String key, String fallback) {
        ResourceBundle b = App.getBundle();
        return b != null && b.containsKey(key) ? b.getString(key) : fallback;
    }

    private String getMotorTypeText(MotorType motorType) {
        if (motorType == null) {
            return "";
        }
        return switch (motorType) {
            case GASOLINE -> tr("motor.gasoline", "Gasoline");
            case DIESEL -> tr("motor.diesel", "Diesel");
            case ELECTRIC -> tr("motor.electric", "Electric");
        };
    }

    private void closeWindow() {
        ((Stage) postButton.getScene().getWindow()).close();
    }

}
