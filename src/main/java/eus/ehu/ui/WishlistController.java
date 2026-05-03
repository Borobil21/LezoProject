package eus.ehu.ui;

import java.util.List;
import java.util.ResourceBundle;

import eus.ehu.businesslogic.BlInterface;
import eus.ehu.domain.MotorType;
import eus.ehu.domain.Sale;
import eus.ehu.domain.Vehicle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class WishlistController {

    @FXML
    private ListView<Vehicle> wishlistListView; // Cambiado de String a Vehicle

    @FXML
    private Label placeholderLabel;

    @FXML
    private Button viewOfferButton; // Añadido el botón para deshabilitarlo si no hay selección

    private BlInterface bl;

    @FXML
    public void initialize() {
        viewOfferButton.setDisable(true);
        setupListView();
        setupSelectionListener();
    }

    public void setBl(BlInterface bl) {
        this.bl = bl;
        loadWishlist();
    }

    @FXML
    private void handleGoToSearch() {
        try {
            App.setRoot("FilterSearch");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewProfile() {
        try {
            App.setRoot("Profile");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewOffer() {
        try {
            Vehicle selectedVehicle = wishlistListView.getSelectionModel().getSelectedItem();
            if (selectedVehicle == null) {
                showError(tr("error.search.select_offer", "Please select an offer from the list first."));
                return;
            }
            App.openViewOffer(selectedVehicle.getListing().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadWishlist() {
        if (bl == null || App.getActualUser() == null) {
            return;
        }

        List<Vehicle> wishlist = bl.getUserWishlist(App.getActualUser().getId());

        if (wishlist == null || wishlist.isEmpty()) {
            wishlistListView.setVisible(false);
            placeholderLabel.setVisible(true);
            placeholderLabel.setText(tr("wishlist.placeholder", "Your wishlist is empty"));
            return;
        }

        wishlistListView.setItems(FXCollections.observableArrayList(wishlist));
        wishlistListView.setVisible(true);
        placeholderLabel.setVisible(false);
    }

    private void setupListView() {
        wishlistListView.setCellFactory(listView -> new ListCell<Vehicle>() {
            @Override
            protected void updateItem(Vehicle v, boolean empty) {
                super.updateItem(v, empty);

                if (empty || v == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    HBox card = new HBox(15);
                    card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #dddddd; -fx-border-radius: 8; -fx-padding: 15;");

                    ImageView imageView = new ImageView();
                    imageView.setFitHeight(120);
                    imageView.setFitWidth(180);

                    String fakeUrl = "https://via.placeholder.com/180x120/cccccc/000000.png?text=" + v.getMake();
                    try {
                        imageView.setImage(new Image(fakeUrl, true));
                    } catch (Exception e) {
                        System.out.println("Could not load image");
                    }
                    VBox detailsBox = new VBox(10);

                    Label titleLabel = new Label(v.getMake() + " " + v.getModel());
                    titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1a1a1a;");

                    // Como la Wishlist guarda Vehicles, sacamos el precio a través de su Listing
                    Sale sale = null;
                    if (v.getListing() instanceof Sale) {
                        sale = (Sale) v.getListing();
                    }
                    
                    String priceText = (sale != null) ? String.format("%.2f €", sale.getFixedPrice()) : "N/A";
                    Label priceLabel = new Label(priceText);
                    priceLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #e60000;");

                    String mileageStr = (v.getMileage() > 0) ? v.getMileage() + " km" : tr("search.not_specified", "Not specified");
                    Label infoLabel = new Label(v.getYear() + "  |  " + mileageStr + "  |  " + getMotorTypeText(v.getMotorType()));
                    infoLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666666;");

                    detailsBox.getChildren().addAll(titleLabel, priceLabel, infoLabel);
                    card.getChildren().addAll(imageView, detailsBox);

                    setGraphic(card);
                    setText(null);
                    setStyle("-fx-background-color: transparent; -fx-padding: 0 0 10 0;");
                }
            }
        });
    }

    private void setupSelectionListener() {
        wishlistListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            viewOfferButton.setDisable(newSelection == null);
        });
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private String tr(String key, String fallback) {
        ResourceBundle b = App.getBundle();
        return b != null && b.containsKey(key) ? b.getString(key) : fallback;
    }

    private String getMotorTypeText(MotorType motorType) {
        if (motorType == null) {
            return tr("search.not_specified", "Not specified");
        }
        return switch (motorType) {
            case GASOLINE -> tr("motor.gasoline", "Gasoline");
            case DIESEL -> tr("motor.diesel", "Diesel");
            case ELECTRIC -> tr("motor.electric", "Electric");
        };
    }
}