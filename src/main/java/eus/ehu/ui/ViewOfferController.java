package eus.ehu.ui;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import eus.ehu.businesslogic.BlInterface;
import eus.ehu.domain.Listing;
import eus.ehu.domain.MotorType;
import eus.ehu.domain.Sale;
import eus.ehu.domain.Vehicle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    private ImageView carImage;
    @FXML
    private Button BuyButton;
    @FXML
    private Button OfferButton;
    @FXML
    private Button WishlistButton;

    private BlInterface bl;
    private Sale selectedSale;
    private Vehicle selectedVehicle;

    @FXML
    public void initialize() {
        applyStyles();
        refreshView();
    }

    private void applyStyles() {
        if (Price != null) {
            Price.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        }
        if (BuyButton != null) {
            BuyButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand;");
        }
        if (OfferButton != null) {
            OfferButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #007bff; -fx-border-color: #007bff; -fx-border-radius: 5; -fx-cursor: hand;");
        }
        if (WishlistButton != null) {
            WishlistButton.setStyle("-fx-border-color: #007bff; -fx-border-radius: 5; -fx-text-fill: #007bff; -fx-cursor: hand;");
        }
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

        if (carImage != null) {
            String imagePath = vehicle.getImagePath();
            try {
                Image image = null;
                if (imagePath != null && !imagePath.isEmpty()) {
                    var imageStream = getClass().getResourceAsStream(imagePath);
                    if (imageStream != null) {
                        image = new Image(imageStream);
                    }
                }
                if (image == null) {
                    var defaultStream = getClass().getResourceAsStream("/images/cars/default_car.jpg");
                    if (defaultStream != null) {
                        image = new Image(defaultStream);
                    }
                }
                carImage.setImage(image);
            } catch (Exception e) {
                System.out.println("Error loading image: " + e.getMessage());
            }
        }

        Model.setText(formatVehicleTitle(vehicle));
        Age.setText(vehicle.getYear() > 0 ? String.valueOf(vehicle.getYear()) : tr("viewoffer.not_available", "N/A"));
        Mileage.setText(formatNumber(vehicle.getMileage()) + " " + tr("viewoffer.unit_km", "km"));
        MotorType.setText(getMotorTypeText(vehicle.getMotorType()));
        HorsePower.setText(formatNumber(vehicle.getHorsepower()) + " " + tr("viewoffer.unit_hp", "hp"));
        Price.setText(sale != null ? formatPrice(sale.getFixedPrice()) : tr("viewoffer.not_available", "N/A"));
        AdditionalDescription.setText(sale != null && sale.getDescription() != null && !sale.getDescription().isBlank()
                ? sale.getDescription()
            : tr("viewoffer.no_description_available", "No description available."));

        if (BuyButton != null) {
            BuyButton.setDisable(false);
        }
        if (OfferButton != null) {
            OfferButton.setDisable(false);
        }
        updateWishlistButtonState();
    }

    public void showEmptyState() {
        Model.setText(tr("viewoffer.no_vehicle_selected", "No vehicle selected"));
        if (carImage != null) carImage.setImage(null);
        Age.setText("-");
        Mileage.setText("-");
        MotorType.setText("-");
        HorsePower.setText("-");
        Price.setText("-");
        AdditionalDescription.setText(tr("viewoffer.select_vehicle_to_see_details", "Select a vehicle to see its details."));

        if (BuyButton != null) BuyButton.setDisable(true);
        if (OfferButton != null) OfferButton.setDisable(true);
    }

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
        if (WishlistButton != null) {
            WishlistButton.setDisable(true);
            WishlistButton.setText(tr("viewoffer.add_wishlist", "Add to wishlist"));
            WishlistButton.setStyle("-fx-background-color: #007bff; -fx-border-color: #007bff; -fx-text-fill: white; -fx-cursor: hand;");
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

    private Sale asSale(Listing listing) {
        return (listing instanceof Sale) ? (Sale) listing : null;
    }

    private String formatVehicleTitle(Vehicle vehicle) {
        String make = vehicle.getMake() != null ? vehicle.getMake() : tr("viewoffer.unknown_make", "Unknown make");
        String model = vehicle.getModel() != null ? vehicle.getModel() : tr("viewoffer.unknown_model", "Unknown model");
        return make + " " + model;
    }

    private String formatPrice(double price) {
        Locale locale = Locale.getDefault();
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        return currencyFormatter.format(price);
    }

    private String formatNumber(int value) {
        return String.valueOf(value);
    }

    private String getMotorTypeText(MotorType motorType) {
        if (motorType == null) {
            return tr("viewoffer.not_available", "N/A");
        }
        return switch (motorType) {
            case GASOLINE -> tr("motor.gasoline", "Gasoline");
            case DIESEL -> tr("motor.diesel", "Diesel");
            case ELECTRIC -> tr("motor.electric", "Electric");
        };
    }

    private String tr(String key, String fallback) {
        java.util.ResourceBundle bundle = App.getBundle();
        return bundle != null && bundle.containsKey(key) ? bundle.getString(key) : fallback;
    }

    private void updateWishlistButtonState() {
        if (WishlistButton == null || selectedVehicle == null || bl == null || App.getActualUser() == null) {
            return;
        }

        Integer currentUserId = App.getActualUser().getId();
        List<Vehicle> wishlist = bl.getUserWishlist(currentUserId);
        boolean isWishlisted = wishlist.stream()
                .anyMatch(vehicle -> selectedVehicle.getLicensePlate() != null
                        && selectedVehicle.getLicensePlate().equals(vehicle.getLicensePlate()));

        setWishlistButtonState(isWishlisted);
    }

    private void setWishlistButtonState(boolean wishlisted) {
        if (WishlistButton == null) {
            return;
        }

        if (wishlisted) {
            WishlistButton.setText(tr("viewoffer.remove_wishlist", "Remove from wishlist"));
            WishlistButton.setStyle("-fx-background-color: white; -fx-border-color: #007bff; -fx-text-fill: #007bff; -fx-cursor: hand;");
        } else {
            WishlistButton.setText(tr("viewoffer.add_wishlist", "Add to wishlist"));
            WishlistButton.setStyle("-fx-background-color: #007bff; -fx-border-color: #007bff; -fx-text-fill: white; -fx-cursor: hand;");
        }
        WishlistButton.setDisable(false);
    }

    @FXML
    private void handleAddToWishlist() {
        if (selectedVehicle != null && bl != null && App.getActualUser() != null) {
            Integer currentUserId = App.getActualUser().getId();
            boolean wishlisted = bl.getUserWishlist(currentUserId).stream()
                    .anyMatch(vehicle -> selectedVehicle.getLicensePlate() != null
                            && selectedVehicle.getLicensePlate().equals(vehicle.getLicensePlate()));

            if (wishlisted) {
                bl.removeFromWishlist(currentUserId, selectedVehicle.getLicensePlate());
                setWishlistButtonState(false);
            } else {
                System.out.println("Intentando añadir coche " + selectedVehicle.getLicensePlate() + " al user " + currentUserId);
                bl.addToWishlist(currentUserId, selectedVehicle.getLicensePlate());
                setWishlistButtonState(true);
            }
        } else {
            System.out.println("Fallo al añadir: selectedVehicle o bl son null");
        }
    }
}