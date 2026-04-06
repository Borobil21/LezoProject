package eus.ehu.ui;

import eus.ehu.businessLogic.BlInterface;
import eus.ehu.domain.Listing;
import eus.ehu.domain.Sale;
import eus.ehu.domain.Vehicle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ViewOfferController {

    @FXML
    private Label Model;
    @FXML
    private Label Age;
    @FXML
    private Label Mileage;
    @FXML
    private Label MotorType;
    @FXML
    private Label HorsePower;
    @FXML
    private Label Price;
    @FXML
    private Label AdditionalDescription;
    @FXML
    private Button BuyButton;
    @FXML
    private Button OfferButton;

    private BlInterface bl;
    private Sale selectedSale;
    private Vehicle selectedVehicle;

    @FXML
    private void handleGoToSearch() {
        try {
            App.setRoot("FilterSearch");
        } catch (Exception e) {
            throw new RuntimeException("Unable to open Filter Search scene", e);
        }
    }

    public void setBl(BlInterface bl) {
        this.bl = bl;
        refreshView();
    }

    public void setSelectedSale(Sale sale) {
        this.selectedSale = sale;
        this.selectedVehicle = sale != null ? sale.getVehicle() : null;
        refreshView();
    }

    public void setSelectedVehicle(Vehicle vehicle) {
        this.selectedVehicle = vehicle;
        this.selectedSale = vehicle != null ? asSale(vehicle.getListing()) : null;
        refreshView();
    }

    @FXML
    public void initialize() {
        refreshView();
    }

    public void refreshView() {
        if (Model == null) {
            return;
        }

        Vehicle vehicle = selectedVehicle;
        Sale sale = selectedSale;

        if (sale != null && vehicle == null) {
            vehicle = sale.getVehicle();
            if (vehicle == null && bl != null && sale.getId() != null) {
                sale = bl.getSaleById(sale.getId());
                vehicle = sale != null ? sale.getVehicle() : null;
                selectedSale = sale;
                selectedVehicle = vehicle;
            }
        }

        if (vehicle == null) {
            showEmptyState();
            return;
        }

        if (sale == null) {
            sale = asSale(vehicle.getListing());
            selectedSale = sale;
        }

        Model.setText(formatVehicleTitle(vehicle));
        Age.setText(vehicle.getYear() > 0 ? String.valueOf(vehicle.getYear()) : "N/A");
        Mileage.setText(formatNumber(vehicle.getMileage()) + " km");
        MotorType.setText(vehicle.getMotorType() != null ? vehicle.getMotorType().name() : "N/A");
        HorsePower.setText(formatNumber(vehicle.getHorsepower()) + " hp");
        Price.setText(sale != null ? formatPrice(sale.getFixedPrice()) : "N/A");
        AdditionalDescription.setText(sale != null && sale.getDescription() != null && !sale.getDescription().isBlank()
                ? sale.getDescription()
                : "No description available.");

        if (BuyButton != null) {
            BuyButton.setDisable(false);
        }
        if (OfferButton != null) {
            OfferButton.setDisable(false);
        }
    }

    public void showEmptyState() {
        Model.setText("No vehicle selected");
        Age.setText("-");
        Mileage.setText("-");
        MotorType.setText("-");
        HorsePower.setText("-");
        Price.setText("-");
        AdditionalDescription.setText("Select a vehicle to see its details.");

        if (BuyButton != null) {
            BuyButton.setDisable(true);
        }
        if (OfferButton != null) {
            OfferButton.setDisable(true);
        }
    }

    private Sale asSale(Listing listing) {
        if (listing instanceof Sale) {
            return (Sale) listing;
        }
        return null;
    }

    private String formatVehicleTitle(Vehicle vehicle) {
        String make = vehicle.getMake() != null ? vehicle.getMake() : "Unknown make";
        String model = vehicle.getModel() != null ? vehicle.getModel() : "Unknown model";
        return make + " " + model;
    }

    private String formatPrice(double price) {
        return String.format("%.2f EUR", price);
    }

    private String formatNumber(int value) {
        return String.valueOf(value);
    }

}