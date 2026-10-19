package ua.edu.nupp.kpp.client;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.nio.file.Path;

public class MainApp extends Application {
    private final HttpHotelClient client = new HttpHotelClient();
    private final ObservableList<Room> roomData = FXCollections.observableArrayList();
    private TableView<Room> roomTable;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Готельний комплекс — Технічні заявки (Варіант №13)");

        TabPane tabPane = new TabPane();
        Tab roomTab = new Tab("Номери");
        roomTab.setClosable(false);
        roomTab.setContent(createRoomPane());

        tabPane.getTabs().add(roomTab);

        Scene scene = new Scene(tabPane, 900, 600);
        stage.setScene(scene);
        stage.show();

        refreshRooms();
    }

    private SplitPane createRoomPane() {
        roomTable = new TableView<>();
        TableColumn<Room, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Room, String> numCol = new TableColumn<>("Номер");
        numCol.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));

        TableColumn<Room, String> catCol = new TableColumn<>("Категорія");
        catCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Room, Integer> floorCol = new TableColumn<>("Поверх");
        floorCol.setCellValueFactory(new PropertyValueFactory<>("floor"));

        roomTable.getColumns().addAll(idCol, numCol, catCol, floorCol);

        VBox form = new VBox(10);
        form.setPadding(new Insets(10));
        TextField numField = new TextField();
        numField.setPromptText("Номер кімнати (напр. 101)");
        TextField catField = new TextField();
        catField.setPromptText("Категорія (напр. Стандарт)");
        TextField floorField = new TextField();
        floorField.setPromptText("Поверх");

        Button addButton = new Button("Додати номер");
        Button importCsvButton = new Button("Імпорт CSV (Task)");
        Button refreshButton = new Button("Оновити");

        ProgressBar progressBar = new ProgressBar(0);
        Label statusLabel = new Label("Готово");

        addButton.setOnAction(e -> {
            try {
                Room r = new Room();
                r.setRoomNumber(numField.getText());
                r.setCategory(catField.getText());
                r.setFloor(Integer.parseInt(floorField.getText()));
                client.addRoom(r);
                refreshRooms();
                numField.clear();
                catField.clear();
                floorField.clear();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        importCsvButton.setOnAction(e -> {
            Path csvPath = Path.of("sample-data", "rooms.csv");
            ImportRoomsTask task = new ImportRoomsTask(csvPath, client);

            progressBar.progressProperty().bind(task.progressProperty());
            statusLabel.textProperty().bind(task.messageProperty());

            task.setOnSucceeded(event -> {
                refreshRooms();
                showAlert("Успіх", "Імпорт завершено успішно!");
            });
            task.setOnFailed(event -> showError(task.getException().getMessage()));

            new Thread(task, "csv-import-thread").start();
        });

        refreshButton.setOnAction(e -> refreshRooms());

        form.getChildren().addAll(
                new Label("Керування номерами"),
                numField, catField, floorField,
                addButton, importCsvButton, refreshButton,
                new Separator(),
                statusLabel, progressBar
        );

        SplitPane splitPane = new SplitPane(roomTable, form);
        splitPane.setDividerPositions(0.65);
        return splitPane;
    }

    private void refreshRooms() {
        try {
            roomData.setAll(client.getRooms());
            roomTable.setItems(roomData);
        } catch (Exception e) {
            System.out.println("Не вдалося завантажити номери: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Помилка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}