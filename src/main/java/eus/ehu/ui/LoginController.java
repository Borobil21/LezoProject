package eus.ehu.ui;

import eus.ehu.businessLogic.BlInterface;
import eus.ehu.businessLogic.BlManager;
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
    private Label lblError;

    @FXML
    private void handleLogin() {
        String email = txtEmail.getText();
        String pass = txtPassword.getText();

        // Call business logic to authenticate user
        User loggedUser = bl.login(email, pass);

        if (loggedUser != null) {
            System.out.println("Welcome, " + loggedUser.getName());
            
            // TODO: Cambiar a la pantalla principal (Main / ViewOffer)
            
            // TODO: Pasar el objeto 'loggedUser' a la siguiente pantalla para saber quién está conectado
            
        } else {
            mostrarErrorTemporal("Email or password incorrect.");
        }
    }

    private void mostrarErrorTemporal(String mensaje) {
        lblError.setText(mensaje);
        lblError.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(e -> lblError.setVisible(false));
        pause.play();
    }
}