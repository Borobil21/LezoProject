package eus.ehu.ui;

import eus.ehu.businesslogic.BlInterface;
import eus.ehu.businesslogic.BlManager;
import eus.ehu.domain.User;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class LoginController {
    // This controller is for the login scene (Login.fxml)

    private BlInterface bl = new BlManager();

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword; // Pwfield to hide password input (yk professionalism)

    @FXML
    private Label InformationLabel;

    @FXML
    private void handleGoToRegister() {
        try {
            App.setRoot("Register");
        } catch (Exception e) {
            throw new RuntimeException("Unable to open Register scene", e);
        }
    }

    @FXML
    private void handleLogin() {
        String email = txtEmail.getText();
        String pass = txtPassword.getText();

        // Call business logic to authenticate user
        User loggedUser = bl.login(email, pass);

        if (loggedUser != null) {
            System.out.println("Welcome, " + loggedUser.getName());
            InformationLabel.setText("Login successful!");
            InformationLabel.setVisible(true);

            try {
                App.setRoot("FilterSearch");
            } catch (Exception e) {
                throw new RuntimeException("Unable to open FilterSearch scene", e);
            }

            // TODO: Pasar el objeto 'loggedUser' a la siguiente pantalla para saber quién
            // está conectado

        } else {
            mostrarErrorTemporal("Email or password incorrect.");
        }
    }

    private void mostrarErrorTemporal(String mensaje) {
        InformationLabel.setText(mensaje);
        InformationLabel.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(e -> InformationLabel.setVisible(false));
        pause.play();
    }
}