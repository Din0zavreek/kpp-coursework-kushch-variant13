package ua.edu.nupp.kpp.client;

import javafx.concurrent.Task;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImportRoomsTask extends Task<Integer> {
    private static final Logger LOG = Logger.getLogger(ImportRoomsTask.class.getName());
    private static final String EXPECTED_HEADER = "room_number;category;floor";

    private final Path filePath;
    private final HttpHotelClient client;

    public ImportRoomsTask(Path filePath, HttpHotelClient client) {
        this.filePath = filePath;
        this.client = client;
    }

    @Override
    protected Integer call() throws Exception {
        LOG.info("Starting background CSV import from file: " + filePath);
        updateMessage("Перевірка CSV-файлу...");
        updateProgress(0, 100);

        if (!Files.exists(filePath)) {
            String err = "CSV-файл не знайдено за шляхом: " + filePath;
            LOG.severe("CSV file not found: " + filePath);
            throw new IllegalArgumentException(err);
        }

        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        if (lines.isEmpty()) {
            String err = "CSV-файл порожній";
            LOG.severe("CSV file is empty");
            throw new IllegalArgumentException(err);
        }

        String header = lines.get(0).replace("\uFEFF", "").trim();
        if (!EXPECTED_HEADER.equalsIgnoreCase(header)) {
            String err = "Некоректний заголовок CSV. Очікувалося: '" + EXPECTED_HEADER + "', отримано: '" + header + "'";
            LOG.severe("Invalid CSV header: expected '" + EXPECTED_HEADER + "', got '" + header + "'");
            throw new IllegalArgumentException(err);
        }

        int total = Math.max(1, lines.size() - 1);
        int imported = 0;
        int skipped = 0;

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split(";");
            if (parts.length != 3) {
                skipped++;
                LOG.warning("Skipped line " + (i + 1) + ": expected 3 columns, got " + parts.length);
                updateProgress(i, total);
                continue;
            }

            try {
                String roomNumber = parts[0].trim();
                String category = parts[1].trim();
                int floor = Integer.parseInt(parts[2].trim());

                Room room = new Room();
                room.setRoomNumber(roomNumber);
                room.setCategory(category);
                room.setFloor(floor);

                client.addRoom(room);
                imported++;
            } catch (NumberFormatException ex) {
                skipped++;
                LOG.warning("Skipped line " + (i + 1) + ": invalid floor number ('" + parts[2].trim() + "')");
            } catch (RuntimeException ex) {
                if (ex.getMessage() != null && ex.getMessage().startsWith("Помилка з'єднання")) {
                    LOG.log(Level.SEVERE, "Import aborted due to server connection error", ex);
                    throw ex;
                }
                skipped++;
                LOG.warning("Skipped line " + (i + 1) + ": rejected by server");
            }

            updateProgress(i, total);
            updateMessage("Імпортовано: " + imported + " із " + total + (skipped > 0 ? " (пропущено: " + skipped + ")" : ""));
        }

        LOG.info("CSV import finished. Imported: " + imported + ", skipped: " + skipped);
        updateMessage("Імпорт завершено. Успішно: " + imported + ", пропущено: " + skipped);
        return imported;
    }
}