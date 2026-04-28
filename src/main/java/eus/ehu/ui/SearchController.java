package eus.ehu.ui;

import java.util.ArrayList;
import java.util.List;

import eus.ehu.businesslogic.BlInterface;
import eus.ehu.domain.MotorType;
import eus.ehu.domain.Sale;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SearchController {

    @FXML private CheckBox porscheCheck;
    @FXML private CheckBox bmwCheck;
    @FXML private CheckBox audiCheck;
    @FXML private CheckBox mercedesCheck;
    @FXML private CheckBox teslaCheck;

    @FXML private Slider priceSlider;
    @FXML private TextField minPriceField;
    @FXML private TextField maxPriceField;

    @FXML private Slider kmSlider;
    @FXML private TextField minKmField;
    @FXML private TextField maxKmField;

    @FXML private CheckBox newCheck;
    @FXML private CheckBox almostNewCheck;
    @FXML private CheckBox usedCheck;
    @FXML private CheckBox oldCheck;

    @FXML private ComboBox<MotorType> motorTypeComboBox;
    @FXML private ComboBox<String> transmissionComboBox;
    @FXML private ComboBox<String> colorComboBox;
    @FXML private ComboBox<String> bodyTypeComboBox;

    @FXML private Label placeholderLabel;


    @FXML private ListView<Sale> resultsListView;
    
    @FXML private Button viewOfferButton;

    private BlInterface bl;

    public void setBl(BlInterface bl) {
        this.bl = bl;
        loadAllVehicles();
    }

    private void loadAllVehicles() {
        List<Sale> allSales = bl.getAllSales();
        showResults(allSales, false); 
    }

    @FXML
    public void initialize() {
        motorTypeComboBox.setItems(FXCollections.observableArrayList(MotorType.values()));
        transmissionComboBox.setItems(FXCollections.observableArrayList("Automatic", "Manual"));
        colorComboBox.setItems(FXCollections.observableArrayList("Black", "White", "Silver", "Red", "Blue", "Grey", "Other"));
        bodyTypeComboBox.setItems(FXCollections.observableArrayList("SUV", "Sedan", "Hatchback", "Coupe", "Convertible", "Van", "Truck"));

        viewOfferButton.setDisable(true);
        setupListView();
        setupSelectionListener();
    }

    private void setupListView() {
        
        resultsListView.setCellFactory(listView -> new ListCell<Sale>() {
            @Override
            protected void updateItem(Sale sale, boolean empty) {
                super.updateItem(sale, empty);

                if (empty || sale == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("-fx-background-color: transparent;"); 
                } else {
                    eus.ehu.domain.Vehicle v = sale.getVehicle();

                    HBox card = new HBox(15);
                    card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #dddddd; -fx-border-radius: 8; -fx-padding: 15;");
                    
                    ImageView imageView = new ImageView();
                    imageView.setFitHeight(120);
                    imageView.setFitWidth(180);
                    
                
                    String fakeUrl = "https://via.placeholder.com/180x120/cccccc/000000.png?text=" + v.getMake();
                    try {
                        imageView.setImage(new Image(fakeUrl, true)); 
                    } catch (Exception e) {
                        System.out.println("Could not load image");
                    }

                    VBox detailsBox = new VBox(10); 

                    Label titleLabel = new Label(v.getMake() + " " + v.getModel());
                    titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1a1a1a;");

                    Label priceLabel = new Label(String.format("%.2f €", sale.getFixedPrice()));
                    priceLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #e60000;");

                    String mileageStr = (v.getMileage() != null) ? v.getMileage() + " km" : "Not specified";
                    Label infoLabel = new Label(v.getYear() + "  |  " + mileageStr + "  |  " + v.getMotorType());
                    infoLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666666;");

                    detailsBox.getChildren().addAll(titleLabel, priceLabel, infoLabel);

                    card.getChildren().addAll(imageView, detailsBox);

                    setGraphic(card);
                    setText(null);
                    
                    setStyle("-fx-background-color: transparent; -fx-padding: 0 0 10 0;"); 
                }
            }
        });
    }

    private void setupSelectionListener() {
        resultsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            viewOfferButton.setDisable(newSelection == null);
        });
    }

    @FXML
    private void handleCreatePost() {
        try {
            App.setRoot("CreatePost");
        } catch (Exception e) {
            throw new RuntimeException("Unable to open Create Post scene", e);
        }
    }

    @FXML
    private void handleViewOffer() {
        try {
            Sale selectedSale = resultsListView.getSelectionModel().getSelectedItem();
            if (selectedSale == null) {
                showError("Please select an offer from the list first.");
                return;
            }
            App.openViewOffer(selectedSale.getId());
        } catch (Exception e) {
            throw new RuntimeException("Unable to open View Offer scene", e);
        }
    }

    @FXML
    private void handleSearch() {
        Double minPrice = parseDouble(minPriceField, "minimum price");
        if (minPrice == null && !minPriceField.getText().isEmpty()) return;

        Double maxPrice = parseDouble(maxPriceField, "maximum price");
        if (maxPrice == null && !maxPriceField.getText().isEmpty()) return;

        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            showError("Minimum price cannot exceed maximum price.");
            return;
        }

        Integer minMileage = parseInteger(minKmField, "minimum mileage");
        if (minMileage == null && !minKmField.getText().isEmpty()) return;

        Integer maxMileage = parseInteger(maxKmField, "maximum mileage");
        if (maxMileage == null && !maxKmField.getText().isEmpty()) return;

        if (minMileage != null && maxMileage != null && minMileage > maxMileage) {
            showError("Minimum mileage cannot exceed maximum mileage.");
            return;
        }

        Integer minYear = null;
        Integer maxYear = null;

        if (newCheck.isSelected()) {
            minYear = earliest(minYear, 2025);
            maxYear = latest(maxYear, 2026);
        }
        if (almostNewCheck.isSelected()) {
            minYear = earliest(minYear, 2021);
            maxYear = latest(maxYear, 2024);
        }
        if (usedCheck.isSelected()) {
            minYear = earliest(minYear, 2006);
            maxYear = latest(maxYear, 2020);
        }
        if (oldCheck.isSelected()) {
            maxYear = latest(maxYear, 2005);
        }

        List<String> makes = new ArrayList<>();
        if (porscheCheck.isSelected()) makes.add("Porsche");
        if (bmwCheck.isSelected()) makes.add("BMW");
        if (audiCheck.isSelected()) makes.add("Audi");
        if (mercedesCheck.isSelected()) makes.add("Mercedes-Benz");
        if (teslaCheck.isSelected()) makes.add("Tesla");

        List<String> makesFilter = makes.isEmpty() ? null : makes;
        MotorType motorType = motorTypeComboBox.getValue();

        List<Sale> results = bl.getFilteredSales(
                null, minPrice, maxPrice, makesFilter, null, 
                minYear, maxYear, minMileage, maxMileage, motorType);

        showResults(results, true);
    }

    @FXML
    void handleClearAll() {
        List.of(porscheCheck, bmwCheck, audiCheck, mercedesCheck, teslaCheck,
                newCheck, almostNewCheck, usedCheck, oldCheck)
                .forEach(cb -> cb.setSelected(false));

        List.of(minPriceField, maxPriceField, minKmField, maxKmField)
                .forEach(TextField::clear);

        motorTypeComboBox.setValue(null);
        transmissionComboBox.setValue(null);
        colorComboBox.setValue(null);
        bodyTypeComboBox.setValue(null);

        loadAllVehicles();
        viewOfferButton.setDisable(true);
    }

    private void showResults(List<Sale> sales, boolean isFilter) {
        if (sales.isEmpty()) {
            resultsListView.setVisible(false);
            placeholderLabel.setText(isFilter ? "No vehicles match your filters." : "There are no vehicles in the database yet.");
            placeholderLabel.setVisible(true);
            
            if (isFilter) {
                showError("No vehicles match your filters.");
            }
            return;
        }

        resultsListView.setItems(FXCollections.observableArrayList(sales));
        resultsListView.setVisible(true);
        placeholderLabel.setVisible(false);
    }

    private Double parseDouble(TextField field, String fieldName) {
        String text = field.getText();
        if (text == null || text.isBlank()) return null;
        try {
            return Double.parseDouble(text.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            showError("Invalid " + fieldName + ": \"" + text + "\"");
            return null;
        }
    }

    private Integer parseInteger(TextField field, String fieldName) {
        String text = field.getText();
        if (text == null || text.isBlank()) return null;
        try {
            return Integer.parseInt(text.trim().replace(".", "").replace(",", ""));
        } catch (NumberFormatException e) {
            showError("Invalid " + fieldName + ": \"" + text + "\"");
            return null;
        }
    }

    private int earliest(Integer current, int candidate) {
        return (current == null) ? candidate : Math.min(current, candidate);
    }

    private int latest(Integer current, int candidate) {
        return (current == null) ? candidate : Math.max(current, candidate);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}