package eus.ehu.ui;

import java.util.Locale;

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
    private eus.ehu.businesslogic.BlInterface bl;

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

    public void setBl(eus.ehu.businesslogic.BlInterface bl) {
        this.bl = bl;
        refreshProfile();
    }

    @FXML
    public void initialize() {
        refreshProfile();
    }

    private void refreshProfile() {
        if (userIdLabel == null) {
            System.out.println("DEBUG: refreshProfile - userIdLabel is null, returning");
            return;
        }

        if (user == null) {
            System.out.println("DEBUG: refreshProfile - user is null");
            userIdLabel.setText("N/A");
            nameLabel.setText("N/A");
            emailLabel.setText("N/A");
            passwordLabel.setText("N/A");
            phoneLabel.setText("N/A");
            locationLabel.setText("N/A");
            publicationsLabel.setText("0");
            return;
        }

        System.out.println("DEBUG: refreshProfile - user id: " + user.getId() + ", bl: " + (bl != null ? "NOT NULL" : "NULL"));
        userIdLabel.setText(user.getId() != null ? String.valueOf(user.getId()) : "N/A");
        nameLabel.setText(user.getName() != null ? user.getName() : "N/A");
        emailLabel.setText(user.getEmail() != null ? user.getEmail() : "N/A");
        passwordLabel.setText(user.getPassword() != null ? user.getPassword() : "N/A");
        phoneLabel.setText(user.getPhone() != null ? user.getPhone() : "N/A");
        locationLabel.setText(user.getLocation() != null ? user.getLocation() : "N/A");
        int count = 0;
        if (bl != null && user.getId() != null) {
            try {
                System.out.println("DEBUG: Calling bl.countUserPublications with userId: " + user.getId());
                count = bl.countUserPublications(user.getId());
                System.out.println("DEBUG: Got count from bl: " + count);
            } catch (Exception e) {
                System.err.println("DEBUG: Exception in bl.countUserPublications:");
                e.printStackTrace();
                // fallback to collection size if available
                count = user.getPublications() != null ? user.getPublications().size() : 0;
                System.out.println("DEBUG: Using fallback count: " + count);
            }
        } else {
            System.out.println("DEBUG: bl is null or userId is null, using collection size");
            count = user.getPublications() != null ? user.getPublications().size() : 0;
        }
        System.out.println("DEBUG: Setting publicationsLabel to: " + count);
        publicationsLabel.setText(String.valueOf(count));
    }

    @FXML
    private void handleViewWishlist() {
        try {
            App.setRoot("Wishlist");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSetLangEu() {
        setLanguage("eu");
    }

    @FXML
    private void handleSetLangEs() {
        setLanguage("es");
    }

    @FXML
    private void handleSetLangEn() {
        setLanguage("en");
    }

    @FXML
    private void handleSetLangFr() {
        setLanguage("fr");
    }

    private void setLanguage(String lang) {
        try {
            Locale locale = new Locale(lang);
            App.setLocale(locale);
            if (this.user != null) {
                this.user.setDefaultLanguage(lang);
                App.setActualUser(this.user);
            }
            App.setRoot("Profile");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}