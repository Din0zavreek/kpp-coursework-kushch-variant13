package ua.edu.nupp.kpp.client;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.nio.file.Path;

public class MainApp extends Application {
    private final HttpHotelClient client = new HttpHotelClient();

    private final ObservableList<Room> roomData = FXCollections.observableArrayList();
    private final ObservableList<Specialist> specialistData = FXCollections.observableArrayList();
    private final ObservableList<Request> requestData = FXCollections.observableArrayList();
    private final ObservableList<ExecutionRecord> recordData = FXCollections.observableArrayList();

    private TableView<Room> roomTable;
    private TableView<Specialist> specialistTable;
    private TableView<Request> requestTable;
    private TableView<ExecutionRecord> recordTable;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Готельний комплекс — Технічні заявки (Варіант №13)");

        TabPane tabPane = new TabPane();

        Tab roomTab = new Tab("Номери", createRoomPane());
        Tab specialistTab = new Tab("Спеціалісти", createSpecialistPane());
        Tab requestTab = new Tab("Заявки", createRequestPane());
        Tab recordTab = new Tab("Виконання", createExecutionRecordPane());

        roomTab.setClosable(false);
        specialistTab.setClosable(false);
        requestTab.setClosable(false);
        recordTab.setClosable(false);

        tabPane.getTabs().addAll(roomTab, specialistTab, requestTab, recordTab);

        Scene scene = new Scene(tabPane, 1000, 650);
        stage.setScene(scene);
        stage.show();

        refreshAllData();
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
        TextField idField = new TextField(); idField.setVisible(false); idField.setManaged(false);
        TextField numField = new TextField(); numField.setPromptText("Номер кімнати");
        TextField catField = new TextField(); catField.setPromptText("Категорія");
        TextField floorField = new TextField(); floorField.setPromptText("Поверх");

        Button saveButton = new Button("Зберегти / Додати");
        Button clearButton = new Button("Очистити форму");
        Button deleteButton = new Button("Видалити вибране");
        Button importCsvButton = new Button("Імпорт CSV");
        Button refreshButton = new Button("Оновити");

        ProgressBar progressBar = new ProgressBar(0);
        Label statusLabel = new Label("Готово");

        roomTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                idField.setText(newVal.getId().toString());
                numField.setText(newVal.getRoomNumber());
                catField.setText(newVal.getCategory());
                floorField.setText(String.valueOf(newVal.getFloor()));
            }
        });

        saveButton.setOnAction(e -> {
            try {
                Room r = new Room();
                if (!idField.getText().isEmpty()) r.setId(Long.parseLong(idField.getText()));
                r.setRoomNumber(numField.getText());
                r.setCategory(catField.getText());
                r.setFloor(Integer.parseInt(floorField.getText()));
                client.addRoom(r); // Серверний метод обробляє і створення, і оновлення
                refreshRooms();
                clearRoomForm(idField, numField, catField, floorField);
            } catch (Exception ex) { showError(ex.getMessage()); }
        });

        clearButton.setOnAction(e -> clearRoomForm(idField, numField, catField, floorField));

        deleteButton.setOnAction(e -> {
            Room selected = roomTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                client.deleteRoom(selected.getId());
                refreshRooms();
            } else {
                showError("Оберіть номер для видалення!");
            }
        });

        importCsvButton.setOnAction(e -> {
            Path csvPath = Path.of("sample-data", "rooms.csv");
            ImportRoomsTask task = new ImportRoomsTask(csvPath, client);
            progressBar.progressProperty().bind(task.progressProperty());
            statusLabel.textProperty().bind(task.messageProperty());
            task.setOnSucceeded(ev -> { refreshRooms(); showAlert("Успіх", "Імпорт завершено!"); });
            task.setOnFailed(ev -> showError(task.getException().getMessage()));
            new Thread(task).start();
        });

        refreshButton.setOnAction(e -> refreshRooms());

        form.getChildren().addAll(new Label("Керування номерами"), idField, numField, catField, floorField,
                saveButton, clearButton, deleteButton, importCsvButton, refreshButton, new Separator(), statusLabel, progressBar);

        SplitPane split = new SplitPane(roomTable, form);
        split.setDividerPositions(0.65);
        return split;
    }

    private void clearRoomForm(TextField id, TextField num, TextField cat, TextField floor) {
        id.clear(); num.clear(); cat.clear(); floor.clear();
        roomTable.getSelectionModel().clearSelection();
    }

    // --- 2. ПАНЕЛЬ СПЕЦІАЛІСТІВ ---
    private SplitPane createSpecialistPane() {
        specialistTable = new TableView<>();
        TableColumn<Specialist, Long> idCol = new TableColumn<>("ID"); idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Specialist, String> nameCol = new TableColumn<>("ПІБ"); nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        TableColumn<Specialist, String> specCol = new TableColumn<>("Спеціалізація"); specCol.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        TableColumn<Specialist, String> phoneCol = new TableColumn<>("Телефон"); phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        specialistTable.getColumns().addAll(idCol, nameCol, specCol, phoneCol);

        VBox form = new VBox(10); form.setPadding(new Insets(10));
        TextField idField = new TextField(); idField.setVisible(false); idField.setManaged(false);
        TextField nameField = new TextField(); nameField.setPromptText("ПІБ спеціаліста");
        TextField specField = new TextField(); specField.setPromptText("Спеціалізація (напр. Сантехнік)");
        TextField phoneField = new TextField(); phoneField.setPromptText("Телефон");

        Button saveButton = new Button("Зберегти / Додати");
        Button deleteButton = new Button("Видалити вибране");
        Button refreshButton = new Button("Оновити");

        specialistTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                idField.setText(newVal.getId().toString());
                nameField.setText(newVal.getFullName());
                specField.setText(newVal.getSpecialization());
                phoneField.setText(newVal.getPhone());
            }
        });

        saveButton.setOnAction(e -> {
            try {
                Specialist s = new Specialist();
                if (!idField.getText().isEmpty()) s.setId(Long.parseLong(idField.getText()));
                s.setFullName(nameField.getText());
                s.setSpecialization(specField.getText());
                s.setPhone(phoneField.getText());
                client.addSpecialist(s);
                refreshSpecialists();
                idField.clear(); nameField.clear(); specField.clear(); phoneField.clear();
            } catch (Exception ex) { showError(ex.getMessage()); }
        });

        deleteButton.setOnAction(e -> {
            Specialist selected = specialistTable.getSelectionModel().getSelectedItem();
            if (selected != null) { client.deleteSpecialist(selected.getId()); refreshSpecialists(); }
            else { showError("Оберіть спеціаліста!"); }
        });

        refreshButton.setOnAction(e -> refreshSpecialists());

        form.getChildren().addAll(new Label("Керування спеціалістами"), idField, nameField, specField, phoneField,
                saveButton, deleteButton, refreshButton);

        SplitPane split = new SplitPane(specialistTable, form);
        split.setDividerPositions(0.65);
        return split;
    }

    // --- 3. ПАНЕЛЬ ЗАЯВОК ---
    private SplitPane createRequestPane() {
        requestTable = new TableView<>();
        TableColumn<Request, Long> idCol = new TableColumn<>("ID"); idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Request, Long> roomCol = new TableColumn<>("ID Номера"); roomCol.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        TableColumn<Request, Long> specCol = new TableColumn<>("ID Спец."); specCol.setCellValueFactory(new PropertyValueFactory<>("specialistId"));
        TableColumn<Request, String> descCol = new TableColumn<>("Опис"); descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<Request, String> stateCol = new TableColumn<>("Стан"); stateCol.setCellValueFactory(new PropertyValueFactory<>("state"));
        TableColumn<Request, Double> priorityCol = new TableColumn<>("Пріоритет (I)"); priorityCol.setCellValueFactory(new PropertyValueFactory<>("priorityIndex"));

        requestTable.getColumns().addAll(idCol, roomCol, specCol, descCol, stateCol, priorityCol);

        VBox form = new VBox(10); form.setPadding(new Insets(10));
        TextField idField = new TextField(); idField.setVisible(false); idField.setManaged(false);
        TextField roomField = new TextField(); roomField.setPromptText("ID Номера");
        TextField specField = new TextField(); specField.setPromptText("ID Спеціаліста");
        TextField descField = new TextField(); descField.setPromptText("Опис проблеми");

        ComboBox<String> stateCombo = new ComboBox<>(FXCollections.observableArrayList("НОВА", "ПРИЗНАЧЕНА", "В_РОБОТІ", "ВИКОНАНО"));
        stateCombo.setPromptText("Оберіть стан");

        TextField f1Field = new TextField("70.0"); f1Field.setPromptText("Фактор F1 (0-100)");
        TextField f2Field = new TextField("60.0"); f2Field.setPromptText("Фактор F2 (0-100)");
        TextField f3Field = new TextField("50.0"); f3Field.setPromptText("Фактор F3 (0-100)");

        Button saveButton = new Button("Зберегти / Додати");
        Button deleteButton = new Button("Видалити вибране");
        Button refreshButton = new Button("Оновити");

        requestTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                idField.setText(newVal.getId().toString());
                roomField.setText(newVal.getRoomId().toString());
                specField.setText(newVal.getSpecialistId() != null ? newVal.getSpecialistId().toString() : "");
                descField.setText(newVal.getDescription());
                stateCombo.setValue(newVal.getState());
                f1Field.setText(String.valueOf(newVal.getF1()));
                f2Field.setText(String.valueOf(newVal.getF2()));
                f3Field.setText(String.valueOf(newVal.getF3()));
            }
        });

        saveButton.setOnAction(e -> {
            try {
                Request r = new Request();
                if (!idField.getText().isEmpty()) r.setId(Long.parseLong(idField.getText()));
                r.setRoomId(Long.parseLong(roomField.getText()));
                r.setSpecialistId(!specField.getText().isEmpty() ? Long.parseLong(specField.getText()) : null);
                r.setDescription(descField.getText());
                r.setState(stateCombo.getValue() != null ? stateCombo.getValue() : "НОВА");
                r.setF1(Double.parseDouble(f1Field.getText()));
                r.setF2(Double.parseDouble(f2Field.getText()));
                r.setF3(Double.parseDouble(f3Field.getText()));

                client.addRequest(r);
                refreshRequests();
            } catch (Exception ex) { showError(ex.getMessage()); }
        });

        deleteButton.setOnAction(e -> {
            Request selected = requestTable.getSelectionModel().getSelectedItem();
            if (selected != null) { client.deleteRequest(selected.getId()); refreshRequests(); }
            else { showError("Оберіть заявку!"); }
        });

        refreshButton.setOnAction(e -> refreshRequests());

        form.getChildren().addAll(new Label("Керування заявками"), idField, roomField, specField, descField, stateCombo,
                new Label("Фактори для розрахунку пріоритету:"), f1Field, f2Field, f3Field,
                saveButton, deleteButton, refreshButton);

        SplitPane split = new SplitPane(requestTable, form);
        split.setDividerPositions(0.65);
        return split;
    }

    // --- 4. ПАНЕЛЬ ВИКОНАННЯ ---
    private SplitPane createExecutionRecordPane() {
        recordTable = new TableView<>();
        TableColumn<ExecutionRecord, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<ExecutionRecord, Long> reqCol = new TableColumn<>("ID Заявки");
        reqCol.setCellValueFactory(new PropertyValueFactory<>("requestId"));

        // Змінено назви властивостей на правильні (executionDate та resultNotes)
        TableColumn<ExecutionRecord, String> dateCol = new TableColumn<>("Дата");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("executionDate"));

        TableColumn<ExecutionRecord, String> notesCol = new TableColumn<>("Нотатки");
        notesCol.setCellValueFactory(new PropertyValueFactory<>("resultNotes"));

        recordTable.getColumns().addAll(idCol, reqCol, dateCol, notesCol);

        VBox form = new VBox(10); form.setPadding(new Insets(10));
        TextField idField = new TextField(); idField.setVisible(false); idField.setManaged(false);
        TextField reqField = new TextField(); reqField.setPromptText("ID Заявки");
        TextField dateField = new TextField(); dateField.setPromptText("Дата (напр. 2026-10-19)");
        TextField notesField = new TextField(); notesField.setPromptText("Результат виконання");

        Button saveButton = new Button("Зберегти / Додати");
        Button deleteButton = new Button("Видалити вибране");
        Button refreshButton = new Button("Оновити");

        // Слухач для заповнення форми при кліку на рядок (для редагування)
        recordTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                idField.setText(newVal.getId() != null ? newVal.getId().toString() : "");
                reqField.setText(newVal.getRequestId() != null ? newVal.getRequestId().toString() : "");
                dateField.setText(newVal.getExecutionDate() != null ? newVal.getExecutionDate() : "");
                notesField.setText(newVal.getResultNotes() != null ? newVal.getResultNotes() : "");
            }
        });

        saveButton.setOnAction(e -> {
            try {
                ExecutionRecord rec = new ExecutionRecord();
                if (!idField.getText().isEmpty()) {
                    rec.setId(Long.parseLong(idField.getText()));
                }
                rec.setRequestId(Long.parseLong(reqField.getText()));
                rec.setExecutionDate(dateField.getText());
                rec.setResultNotes(notesField.getText());

                client.addExecutionRecord(rec);
                refreshRecords();

                // Очищення форми після збереження
                idField.clear();
                reqField.clear();
                dateField.clear();
                notesField.clear();
                recordTable.getSelectionModel().clearSelection();
            } catch (Exception ex) {
                showError("Помилка збереження: " + ex.getMessage());
            }
        });

        deleteButton.setOnAction(e -> {
            ExecutionRecord selected = recordTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                client.deleteExecutionRecord(selected.getId());
                refreshRecords();
            } else {
                showError("Оберіть запис для видалення!");
            }
        });

        refreshButton.setOnAction(e -> refreshRecords());

        form.getChildren().addAll(new Label("Записи виконання"), idField, reqField, dateField, notesField,
                saveButton, deleteButton, refreshButton);

        SplitPane split = new SplitPane(recordTable, form);
        split.setDividerPositions(0.65);
        return split;
    }

    // --- МЕТОДИ ОТРИМАННЯ ДАНИХ З КЛІЄНТА ---
    private void refreshAllData() {
        refreshRooms();
        refreshSpecialists();
        refreshRequests();
        refreshRecords();
    }

    private void refreshRooms() {
        try { roomData.setAll(client.getRooms()); roomTable.setItems(roomData); } catch (Exception e) { }
    }
    private void refreshSpecialists() {
        try { specialistData.setAll(client.getSpecialists()); specialistTable.setItems(specialistData); } catch (Exception e) { }
    }
    private void refreshRequests() {
        try { requestData.setAll(client.getRequests()); requestTable.setItems(requestData); } catch (Exception e) { }
    }
    private void refreshRecords() {
        try { recordData.setAll(client.getExecutionRecords()); recordTable.setItems(recordData); } catch (Exception e) { }
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK); a.showAndWait();
    }
    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK); a.setHeaderText(title); a.showAndWait();
    }

    public static void main(String[] args) { launch(args); }
}