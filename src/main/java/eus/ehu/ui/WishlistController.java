package eus.ehu.ui;

import java.util.List;
import java.util.stream.Collectors;

import eus.ehu.businesslogic.BlInterface;
import eus.ehu.domain.Vehicle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class WishlistController {

    @FXML
    private ListView<String> wishlistListView;

    @FXML
    private Label placeholderLabel;

    @FXML
    private TextField SaleIdField;

    private BlInterface bl;

    public void setBl(BlInterface bl) {
        this.bl = bl;
        loadWishlist(); // Carga los coches al entrar a la pantalla
    }

    // Botón para volver atrás
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

    // Botón para ver el coche seleccionado
    @FXML
    private void handleViewOffer() {
        try {
            String saleIdText = SaleIdField.getText();
            if (saleIdText == null || saleIdText.isBlank()) {
                showError("Por favor introduce un ID de venta.");
                return;
            }
            App.openViewOffer(Integer.parseInt(saleIdText.trim()));
        } catch (NumberFormatException e) {
            showError("El ID debe ser un número.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadWishlist() {
        System.out.println("Cargando pantalla Wishlist...");
    
        if (bl == null) {
            System.out.println("❌ ERROR: bl es nulo. Falta inyectarlo en App.java");
            return;
        }
        if (App.getActualUser() == null) {
            System.out.println("❌ ERROR: No hay usuario logueado.");
            return;
        }

        List<Vehicle> wishlist = bl.getUserWishlist(App.getActualUser().getId());
        System.out.println("✅ Coches encontrados en la BBDD para este usuario: " + wishlist.size());

        if (wishlist == null || wishlist.isEmpty()) {
            wishlistListView.setVisible(false);
            placeholderLabel.setVisible(true);
            placeholderLabel.setText("Tu Wishlist está vacía.");
            return;
        }

        // Formateamos cómo se ve cada línea, sacando el ID del Listing para poder ir a la oferta
        List<String> display = wishlist.stream()
                .map(v -> String.format("ID: %d | %s %s (%d) - %d km",
                        v.getListing().getId(),
                        v.getMake(),
                        v.getModel(),
                        v.getYear(),
                        v.getMileage()))
                .collect(Collectors.toList());

        wishlistListView.setItems(FXCollections.observableArrayList(display));
        wishlistListView.setVisible(true);
        placeholderLabel.setVisible(false);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}