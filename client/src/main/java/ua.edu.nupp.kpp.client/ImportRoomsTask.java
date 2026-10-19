package ua.edu.nupp.kpp.client;

import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImportRoomsTask extends Task<Integer> {
    private final Path filePath;
    private final HttpHotelClient client;

    public ImportRoomsTask(Path filePath, HttpHotelClient client) {
        this.filePath = filePath;
        this.client = client;
    }

    @Override
    protected Integer call() throws Exception {
        updateMessage("Читання CSV-файлу...");
        updateProgress(0, 100);

        if (!Files.exists(filePath)) {
            throw new IllegalArgumentException("Файл не знайдено: " + filePath.toAbsolutePath());
        }

        int importedCount = 0;
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            boolean isHeader = true;
            int totalLines = (int) Files.lines(filePath).count();
            int currentLine = 0;

            while ((line = reader.readLine()) != null) {
                currentLine++;
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(";");
                if (parts.length >= 3) {
                    Room room = new Room();
                    room.setRoomNumber(parts[0].trim());
                    room.setCategory(parts[1].trim());
                    room.setFloor(Integer.parseInt(parts[2].trim()));

                    client.addRoom(room);
                    importedCount++;
                }

                updateProgress(currentLine, totalLines);
                updateMessage("Імпортовано рядків: " + importedCount);
            }
        }

        updateMessage("Готово. Імпортовано: " + importedCount);
        return importedCount;
    }
}