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
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController {
    // This controller is for the login scene (Login.fxml)

    private BlInterface bl = BlManager.getInstance();

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
            // If user already logged in, persist in memory
            User current = App.getActualUser();
            if (current != null) {
                current.setDefaultLanguage(lang);
                App.setActualUser(current);
            }
            App.setRoot("Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogin() {
        String email = txtEmail.getText();
        String pass = txtPassword.getText();

        // Call business logic to authenticate user
        User loggedUser = bl.login(email, pass);

        if (loggedUser != null) {
            App.setActualUser(loggedUser); // Store the logged-in user in the App class for global access
            System.out.println("Welcome, " + loggedUser.getName());
            ResourceBundle b = App.getBundle();
            InformationLabel.setText(b != null ? b.getString("message.login_success") : "Login successful!");
            InformationLabel.setVisible(true);

            try {
                App.setRoot("FilterSearch");
            } catch (Exception e) {
                throw new RuntimeException("Unable to open FilterSearch scene", e);
            }

            // TODO: Pasar el objeto 'loggedUser' a la siguiente pantalla para saber quién
            // está conectado

        } else {
            ResourceBundle b = App.getBundle();
            mostrarErrorTemporal(b != null ? b.getString("error.login_invalid_credentials") : "Email or password incorrect.");
        }
    }

    private void mostrarErrorTemporal(String mensaje) {
        InformationLabel.setText(mensaje);
        InformationLabel.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(e -> InformationLabel.setVisible(false));
        pause.play();
    }

    @FXML
    private void handleOierLogin() {
        txtEmail.setText("oier@gmail.com");
        txtPassword.setText("oier");
        handleLogin();
    }

    @FXML
    private void handlePabloLogin() {
        txtEmail.setText("pablo@gmail.com");
        txtPassword.setText("pablo");
        handleLogin();
    }

    @FXML
    private void handleRaulLogin() {
        txtEmail.setText("raul@gmail.com");
        txtPassword.setText("raul");
        handleLogin();
    }

    @FXML
    private void handleJonLogin() {
        txtEmail.setText("jon@gmail.com");
        txtPassword.setText("jon");
        handleLogin();
    }


}