package eus.ehu.ui;

import eus.ehu.domain.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProfileController {

    @FXML
    private Label userIdLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label locationLabel;

    @FXML
    private Label publicationsLabel;

    private User user;

    @FXML
    private void handleGoToSearch() {
        try {
            App.setRoot("FilterSearch");
        } catch (Exception e) {
            throw new RuntimeException("Unable to open Filter Search scene", e);
        }
    }

    public void setUser(User user) {
        this.user = user;
        refreshProfile();
    }

    @FXML
    public void initialize() {
        refreshProfile();
    }

    private void refreshProfile() {
        if (userIdLabel == null) {
            return;
        }

        if (user == null) {
            userIdLabel.setText("N/A");
            nameLabel.setText("N/A");
            emailLabel.setText("N/A");
            passwordLabel.setText("N/A");
            phoneLabel.setText("N/A");
            locationLabel.setText("N/A");
            publicationsLabel.setText("0");
            return;
        }

        userIdLabel.setText(user.getId() != null ? String.valueOf(user.getId()) : "N/A");
        nameLabel.setText(user.getName() != null ? user.getName() : "N/A");
        emailLabel.setText(user.getEmail() != null ? user.getEmail() : "N/A");
        passwordLabel.setText(user.getPassword() != null ? user.getPassword() : "N/A");
        phoneLabel.setText(user.getPhone() != null ? user.getPhone() : "N/A");
        locationLabel.setText(user.getLocation() != null ? user.getLocation() : "N/A");
        publicationsLabel.setText(user.getPublications() != null ? String.valueOf(user.getPublications().size()) : "0");
    }

    @FXML
    private void handleViewWishlist() {
        try {
            App.setRoot("Wishlist");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}