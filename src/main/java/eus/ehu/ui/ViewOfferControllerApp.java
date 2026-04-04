package eus.ehu.ui;

import java.io.IOException;

import eus.ehu.businesslogic.BlInterface;
import eus.ehu.businesslogic.BlManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewOfferControllerApp extends Application {

    private static final String FXML_BASE_PATH = "/eus/ehu/presentation/";
    private static Scene scene;

    private final int saleId = 1;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_BASE_PATH + "ViewOffer.fxml"));
        scene = new Scene(loader.load(), 1000, 800);
        
        ViewOfferController controller = loader.getController();
        BlInterface bl = new BlManager();
        controller.setBl(bl);
        controller.setSelectedSale(bl.getSaleById(saleId));
        controller.setSelectedVehicle(bl.getVehicleBySaleId(saleId));
        controller.refreshView();
        //controller.showEmptyState();

        stage.setScene(scene);
        stage.setTitle("View Offer");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}