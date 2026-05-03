package eus.ehu.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import eus.ehu.businesslogic.BlInterface;
import eus.ehu.domain.MotorType;
import eus.ehu.domain.Sale;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SearchController {

    @FXML private CheckBox porscheCheck;
    @FXML private CheckBox bmwCheck;
    @FXML private CheckBox audiCheck;
    @FXML private CheckBox mercedesCheck;
    @FXML private CheckBox teslaCheck;

    @FXML private Slider minPriceSlider;
    @FXML private Slider maxPriceSlider;
    @FXML private TextField minPriceField;
    @FXML private TextField maxPriceField;

    @FXML private Slider minKmSlider;
    @FXML private Slider maxKmSlider;
    @FXML private TextField minKmField;
    @FXML private TextField maxKmField;

    @FXML private CheckBox newCheck;
    @FXML private CheckBox almostNewCheck;
    @FXML private CheckBox usedCheck;
    @FXML private CheckBox oldCheck;

    @FXML private ComboBox<MotorType> motorTypeComboBox;

    @FXML private Label placeholderLabel;
    @FXML private Label showMoreBrandsLabel;
    @FXML private ListView<Sale> resultsListView;

    @FXML private Button profileName;

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

        // Price sliders ↔ text fields
        minPriceSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() > maxPriceSlider.getValue())
                minPriceSlider.setValue(maxPriceSlider.getValue());
            else
                minPriceField.setText(String.format("%.0f", newVal.doubleValue()));
        });
        maxPriceSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() < minPriceSlider.getValue())
                maxPriceSlider.setValue(minPriceSlider.getValue());
            else
                maxPriceField.setText(String.format("%.0f", newVal.doubleValue()));
        });
        minPriceField.textProperty().addListener((obs, o, newVal) -> {
            try { minPriceSlider.setValue(Double.parseDouble(newVal.replace(",", "."))); }
            catch (NumberFormatException ignored) {}
        });
        maxPriceField.textProperty().addListener((obs, o, newVal) -> {
            try { maxPriceSlider.setValue(Double.parseDouble(newVal.replace(",", "."))); }
            catch (NumberFormatException ignored) {}
        });

        // Km sliders ↔ text fields
        minKmSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() > maxKmSlider.getValue())
                minKmSlider.setValue(maxKmSlider.getValue());
            else
                minKmField.setText(String.format("%.0f", newVal.doubleValue()));
        });
        maxKmSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() < minKmSlider.getValue())
                maxKmSlider.setValue(minKmSlider.getValue());
            else
                maxKmField.setText(String.format("%.0f", newVal.doubleValue()));
        });
        minKmField.textProperty().addListener((obs, o, newVal) -> {
            try { minKmSlider.setValue(Double.parseDouble(newVal.replace(",", "."))); }
            catch (NumberFormatException ignored) {}
        });
        maxKmField.textProperty().addListener((obs, o, newVal) -> {
            try { maxKmSlider.setValue(Double.parseDouble(newVal.replace(",", "."))); }
            catch (NumberFormatException ignored) {}
        });

        setupListView();
    }

    // ──────────────────────────────────────────────────────────────
    // Show all brands dialog
    // ──────────────────────────────────────────────────────────────
    @FXML
    private void handleShowMoreBrands() {
        // Count cars per brand from all sales
        Map<String, Integer> brandCount = new TreeMap<>();
        for (Sale sale : bl.getAllSales()) {
            if (sale.getVehicle() != null && sale.getVehicle().getMake() != null) {
                String make = sale.getVehicle().getMake();
                brandCount.merge(make, 1, Integer::sum);
            }
        }

        // Build dialog content
        VBox brandList = new VBox(6);
        brandList.setPadding(new Insets(8, 16, 8, 16));

        for (Map.Entry<String, Integer> entry : brandCount.entrySet()) {
            HBox row = new HBox();
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(6, 8, 6, 8));
            row.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 6;");

            Label brandLabel = new Label(entry.getKey());
            brandLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #222222;");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Label countLabel = new Label(entry.getValue() + " listing" + (entry.getValue() > 1 ? "s" : ""));
            countLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #007bff; -fx-font-weight: bold;");

            row.getChildren().addAll(brandLabel, spacer, countLabel);
            brandList.getChildren().add(row);
        }

        if (brandCount.isEmpty()) {
            Label empty = new Label("No brands available.");
            empty.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 13px;");
            brandList.getChildren().add(empty);
        }

        ScrollPane scroll = new ScrollPane(brandList);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scroll.setPrefHeight(400);

        // Header
        Label title = new Label("All available brands");
        title.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1a1a1a;");
        title.setPadding(new Insets(16, 16, 8, 16));

        Label subtitle = new Label(brandCount.size() + " brands · " +
                brandCount.values().stream().mapToInt(i -> i).sum() + " listings total");
        subtitle.setStyle("-fx-font-size: 12px; -fx-text-fill: #888888;");
        subtitle.setPadding(new Insets(0, 16, 12, 16));

        Button closeBtn = new Button("Close");
        closeBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        closeBtn.setPrefWidth(100);

        VBox container = new VBox(0, title, subtitle, scroll);
        VBox.setMargin(closeBtn, new Insets(10, 16, 16, 16));
        container.getChildren().add(closeBtn);
        container.setStyle("-fx-background-color: white;");

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("All Brands");
        dialog.setResizable(false);
        dialog.setScene(new Scene(container, 340, 520));

        closeBtn.setOnAction(e -> dialog.close());

        dialog.showAndWait();
    }

    // ──────────────────────────────────────────────────────────────
    // List view setup
    // ──────────────────────────────────────────────────────────────
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
                    card.setStyle("-fx-background-color: white; -fx-background-radius: 8; " +
                            "-fx-border-color: #dddddd; -fx-border-radius: 8; -fx-padding: 15; -fx-cursor: hand;");

                    ImageView imageView = new ImageView();
                    imageView.setFitHeight(120);
                    imageView.setFitWidth(180);

                    String imagePath = v.getImagePath();
                    try {
                        var stream = imagePath != null && !imagePath.isEmpty()
                                ? getClass().getResourceAsStream(imagePath) : null;
                        imageView.setImage(new Image(
                                stream != null ? stream
                                        : getClass().getResourceAsStream("/images/cars/default_car.jpg")));
                    } catch (Exception e) {
                        try {
                            imageView.setImage(new Image(
                                    getClass().getResourceAsStream("/images/cars/default_car.jpg")));
                        } catch (Exception ignored) {}
                    }

                    VBox details = new VBox(10);
                    Label title = new Label(v.getMake() + " " + v.getModel());
                    title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1a1a1a;");
                    Label price = new Label(String.format("%.2f €", sale.getFixedPrice()));
                    price.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #e60000;");
                    String mileageStr = v.getMileage() != null ? v.getMileage() + " km" : "Not specified";
                    Label info = new Label(v.getYear() + "  |  " + mileageStr + "  |  " + v.getMotorType());
                    info.setStyle("-fx-font-size: 14px; -fx-text-fill: #666666;");
                    details.getChildren().addAll(title, price, info);
                    card.getChildren().addAll(imageView, details);

                    card.setOnMouseClicked(event -> {
                        try { App.openViewOffer(sale.getId()); }
                        catch (Exception e) { e.printStackTrace(); }
                    });

                    setGraphic(card);
                    setText(null);
                }
            }
        });
    }

    // ──────────────────────────────────────────────────────────────
    // Navigation
    // ──────────────────────────────────────────────────────────────
    @FXML
    private void handleViewProfile() {
        try { App.setRoot("Profile"); }
        catch (Exception e) { throw new RuntimeException("Unable to open Profile scene", e); }
    }

    @FXML
    private void handleCreatePost() {
        try { App.setRoot("CreatePost"); }
        catch (Exception e) { throw new RuntimeException("Unable to open Create Post scene", e); }
    }

    // ──────────────────────────────────────────────────────────────
    // Search & filter
    // ──────────────────────────────────────────────────────────────
    @FXML
    private void handleSearch() {
        Double minPrice = parseDouble(minPriceField, "minimum price");
        if (minPrice == null && !minPriceField.getText().isEmpty()) return;
        Double maxPrice = parseDouble(maxPriceField, "maximum price");
        if (maxPrice == null && !maxPriceField.getText().isEmpty()) return;
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            showError("Minimum price cannot exceed maximum price."); return;
        }

        Integer minMileage = parseInteger(minKmField, "minimum mileage");
        if (minMileage == null && !minKmField.getText().isEmpty()) return;
        Integer maxMileage = parseInteger(maxKmField, "maximum mileage");
        if (maxMileage == null && !maxKmField.getText().isEmpty()) return;
        if (minMileage != null && maxMileage != null && minMileage > maxMileage) {
            showError("Minimum mileage cannot exceed maximum mileage."); return;
        }

        Integer minYear = null, maxYear = null;
        if (newCheck.isSelected())       { minYear = earliest(minYear, 2025); maxYear = latest(maxYear, 2026); }
        if (almostNewCheck.isSelected()) { minYear = earliest(minYear, 2021); maxYear = latest(maxYear, 2024); }
        if (usedCheck.isSelected())      { minYear = earliest(minYear, 2006); maxYear = latest(maxYear, 2020); }
        if (oldCheck.isSelected())       { maxYear = latest(maxYear, 2005); }

        List<String> makes = new ArrayList<>();
        if (porscheCheck.isSelected())  makes.add("Porsche");
        if (bmwCheck.isSelected())      makes.add("BMW");
        if (audiCheck.isSelected())     makes.add("Audi");
        if (mercedesCheck.isSelected()) makes.add("Mercedes-Benz");
        if (teslaCheck.isSelected())    makes.add("Tesla");

        List<Sale> results = bl.getFilteredSales(
                null, minPrice, maxPrice, makes.isEmpty() ? null : makes, null,
                minYear, maxYear, minMileage, maxMileage, motorTypeComboBox.getValue());

        showResults(results, true);
    }

    @FXML
    void handleClearAll() {
        List.of(porscheCheck, bmwCheck, audiCheck, mercedesCheck, teslaCheck,
                newCheck, almostNewCheck, usedCheck, oldCheck)
                .forEach(cb -> cb.setSelected(false));

        minPriceSlider.setValue(0);
        maxPriceSlider.setValue(200000);
        minKmSlider.setValue(0);
        maxKmSlider.setValue(500000);

        List.of(minPriceField, maxPriceField, minKmField, maxKmField).forEach(TextField::clear);
        motorTypeComboBox.setValue(null);
        loadAllVehicles();
    }

    // ──────────────────────────────────────────────────────────────
    // Helpers
    // ──────────────────────────────────────────────────────────────
    private void showResults(List<Sale> sales, boolean isFilter) {
        if (sales.isEmpty()) {
            resultsListView.setVisible(false);
            placeholderLabel.setText(isFilter
                    ? "No vehicles match your filters."
                    : "There are no vehicles in the database yet.");
            placeholderLabel.setVisible(true);
            if (isFilter) showError("No vehicles match your filters.");
            return;
        }
        resultsListView.setItems(FXCollections.observableArrayList(sales));
        resultsListView.setVisible(true);
        placeholderLabel.setVisible(false);
    }

    private Double parseDouble(TextField field, String name) {
        String text = field.getText();
        if (text == null || text.isBlank()) return null;
        try { return Double.parseDouble(text.trim().replace(",", ".")); }
        catch (NumberFormatException e) { showError("Invalid " + name + ": \"" + text + "\""); return null; }
    }

    private Integer parseInteger(TextField field, String name) {
        String text = field.getText();
        if (text == null || text.isBlank()) return null;
        try { return Integer.parseInt(text.trim().replace(".", "").replace(",", "")); }
        catch (NumberFormatException e) { showError("Invalid " + name + ": \"" + text + "\""); return null; }
    }

    private int earliest(Integer current, int candidate) {
        return current == null ? candidate : Math.min(current, candidate);
    }

    private int latest(Integer current, int candidate) {
        return current == null ? candidate : Math.max(current, candidate);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
