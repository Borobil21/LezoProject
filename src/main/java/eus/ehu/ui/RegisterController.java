package eus.ehu.ui;

import eus.ehu.businesslogic.BlInterface;
import eus.ehu.businesslogic.BlManager;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RegisterController {
    //this controler is for the register scene

    private BlInterface bl = new BlManager();

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
    private Label ErrorLabel;

    @FXML
    private void handleRegister() {
        String name = txtName.getText();
        String email = txtEmail.getText();
        String pass = txtPassword.getText();
        String phone = txtPhone.getText();
        String loc = txtLocation.getText();

        // bl is BlManager instance
        boolean success = bl.register(name, email, pass, phone, loc);

        if (success) {
            System.out.println("¡User succesfully registered!");
            // Here'll change to the login scene, but for now just print a message
        } else {
            // Show error message for 5 seconds
            ErrorLabel.setVisible(true);
            PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(5));
            pause.setOnFinished(e -> ErrorLabel.setVisible(false));
            pause.play();
        }
    }
}
