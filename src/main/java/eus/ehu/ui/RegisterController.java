package eus.ehu.ui;

import eus.ehu.businesslogic.BlInterface;
import eus.ehu.businesslogic.BlManager;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RegisterController {
    // this controler is for the register scene (Resgister.fxml)

    private BlInterface bl = BlManager.getInstance();

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtLocation;

    @FXML
    private Label InformationLabel;

    @FXML
    private void handleGoToLogin() {
        try {
            App.setRoot("Login");
        } catch (Exception e) {
            throw new RuntimeException("Unable to open Login scene", e);
        }
    }

    @FXML
    private void handleRegister() {
        String name = txtName.getText();
        String email = txtEmail.getText();
        String pass = txtPassword.getText();
        String phone = txtPhone.getText();
        String loc = txtLocation.getText();

        // bl is BlManager instance
        boolean success = bl.register(name, email, pass, phone, loc);

        App.setActualUser(bl.getUserByEmail(email)); // Set the actual user in App after registration

        if (success) {
            System.out.println("¡User succesfully registered!");
            InformationLabel.setText("User succesfully registered!");
            InformationLabel.setVisible(true);

            try {
                App.setRoot("FilterSearch");
            } catch (Exception e) {
                throw new RuntimeException("Unable to open FilterSearch scene", e);
            }

        } else {
            // Show error message for 5 seconds
            InformationLabel.setText("Registration failed. Please check your information.");
            InformationLabel.setVisible(true);
            PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(5));
            pause.setOnFinished(e -> InformationLabel.setVisible(false));
            pause.play();
        }
    }
}
