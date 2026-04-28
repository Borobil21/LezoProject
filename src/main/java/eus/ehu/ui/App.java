    package eus.ehu.ui;

    import java.io.IOException;

    import eus.ehu.businesslogic.BlInterface;
    import eus.ehu.businesslogic.BlManager;
    import eus.ehu.domain.Sale;
    import eus.ehu.domain.User;
    import javafx.application.Application;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.stage.Stage;

    public class App extends Application {

        private static final String FXML_BASE_PATH = "/eus/ehu/presentation/";
        private static Scene scene;
        private static Integer selectedViewOfferSaleId;
        private static BlInterface bl;
        private static User actualUser;

        @Override
        public void start(Stage stage) throws IOException {
            bl = BlManager.getInstance();
            try {
                actualUser = bl.getDefaultUser();
            } catch (Exception e) {
                actualUser = null;
                System.err.println("Warning: could not load default user — DB may be unavailable: " + e.getMessage());
            }

            scene = new Scene(loadFXML("Register"), 1000, 800);
            stage.setScene(scene);
            stage.show();
        }

        public static void setRoot(String fxml) throws IOException {
            scene.setRoot(loadFXML(fxml));
        }

        public static void openViewOffer(Integer saleId) throws IOException {
            selectedViewOfferSaleId = saleId;
            setRoot("ViewOffer");
        }

        private static Parent loadFXML(String fxml) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(FXML_BASE_PATH + fxml + ".fxml"));
            Parent root = fxmlLoader.load();

            if ("ViewOffer".equals(fxml)) {
                ViewOfferController controller = fxmlLoader.getController();
                controller.setBl(bl);

                if (selectedViewOfferSaleId != null) {
                    Sale sale = bl.getSaleById(selectedViewOfferSaleId);
                    controller.setSelectedSale(sale);
                }
            } else if ("FilterSearch".equals(fxml)) {
                SearchController controller = fxmlLoader.getController();
                controller.setBl(bl);
            } else if ("Profile".equals(fxml)) {
                ProfileController controller = fxmlLoader.getController();
                controller.setUser(actualUser);
            } else if ("Wishlist".equals(fxml)) {
                WishlistController controller = fxmlLoader.getController();
                controller.setBl(bl);
            }

            return root;
        }

        public static User getActualUser() {
            return actualUser;
        }

        public static void setActualUser(User user) {
            actualUser = user;
        }

        public static void main(String[] args) {
            launch();
        }

    }