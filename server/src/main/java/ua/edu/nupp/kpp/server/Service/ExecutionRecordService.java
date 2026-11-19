package ua.edu.nupp.kpp.server.Service;

import ua.edu.nupp.kpp.server.DAO.ExecutionRecordDAO;
import ua.edu.nupp.kpp.server.DAO.RequestDAO;
import ua.edu.nupp.kpp.server.Models.ExecutionRecord;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExecutionRecordService {
    private static final Logger LOGGER = Logger.getLogger(ExecutionRecordService.class.getName());
    private final ExecutionRecordDAO recordDAO = new ExecutionRecordDAO();
    private final RequestDAO requestDAO = new RequestDAO();

    public List<ExecutionRecord> getAllRecords() {
        try {
            List<ExecutionRecord> list = recordDAO.getAll();
            LOGGER.info("Отримано записи виконання. Кількість: " + list.size());
            return list;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Помилка отримання записів виконання", e);
            throw new RuntimeException("Помилка БД при отриманні записів виконання: " + e.getMessage());
        }
    }

    public void addRecord(ExecutionRecord rec) {
        validateRecord(rec);
        try {
            validateRequestExists(rec.getRequestId());
            recordDAO.insert(rec);
            LOGGER.info("Додано запис виконання для заявки ID=" + rec.getRequestId());
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Помилка валідації запису виконання: " + e.getMessage());
            throw e;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Помилка БД при додаванні запису виконання", e);
            throw new RuntimeException("Помилка БД при додаванні запису виконання: " + e.getMessage());
        }
    }

    public void updateRecord(ExecutionRecord rec) {
        if (rec.getId() == null) {
            throw new IllegalArgumentException("ID запису виконання не може бути порожнім при оновленні");
        }
        validateRecord(rec);
        try {
            validateRequestExists(rec.getRequestId());
            recordDAO.update(rec);
            LOGGER.info("Оновлено запис виконання ID=" + rec.getId());
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Помилка валідації при оновленні запису виконання: " + e.getMessage());
            throw e;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Помилка БД при оновленні запису виконання ID=" + rec.getId(), e);
            throw new RuntimeException("Помилка БД при оновленні запису виконання: " + e.getMessage());
        }
    }

    public void deleteRecord(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID запису виконання не може бути порожнім при видаленні");
        }
        try {
            recordDAO.delete(id);
            LOGGER.info("Видалено запис виконання ID=" + id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Помилка видалення запису виконання ID=" + id, e);
            throw new RuntimeException("Помилка БД при видаленні запису виконання: " + e.getMessage());
        }
    }

    private void validateRecord(ExecutionRecord rec) {
        if (rec == null) {
            throw new IllegalArgumentException("Запис виконання не може бути порожнім");
        }
        if (rec.getRequestId() == null) {
            throw new IllegalArgumentException("ID заявки є обов'язковим");
        }
        if (rec.getExecutionDate() == null || rec.getExecutionDate().trim().isEmpty()) {
            throw new IllegalArgumentException("Дата виконання є обов'язковою");
        }
        if (rec.getResultNotes() == null || rec.getResultNotes().trim().isEmpty()) {
            throw new IllegalArgumentException("Примітки про результат виконання є обов'язковими");
        }
    }

    private void validateRequestExists(Long requestId) throws SQLException {
        boolean exists = requestDAO.getAll().stream().anyMatch(req -> req.getId().equals(requestId));
        if (!exists) {
            throw new IllegalArgumentException("Заявку з ID=" + requestId + " не знайдено в базі даних");
        }
    }
}