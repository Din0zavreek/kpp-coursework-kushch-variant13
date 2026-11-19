package ua.edu.nupp.kpp.server.Service;

import ua.edu.nupp.kpp.server.DAO.SpecialistDAO;
import ua.edu.nupp.kpp.server.Models.Specialist;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpecialistService {
    private static final Logger LOGGER = Logger.getLogger(SpecialistService.class.getName());
    private final SpecialistDAO specialistDAO = new SpecialistDAO();

    public List<Specialist> getAllSpecialists() {
        try {
            List<Specialist> list = specialistDAO.getAll();
            LOGGER.info("Отримано список спеціалістів. Кількість: " + list.size());
            return list;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Помилка отримання списку спеціалістів", e);
            throw new RuntimeException("Помилка БД при отриманні спеціалістів: " + e.getMessage());
        }
    }

    public void addSpecialist(Specialist s) {
        validateSpecialist(s);
        try {
            specialistDAO.insert(s);
            LOGGER.info("Додано спеціаліста: " + s.getFullName() + " (" + s.getSpecialization() + ")");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Помилка додавання спеціаліста", e);
            throw new RuntimeException("Помилка БД при додаванні спеціаліста: " + e.getMessage());
        }
    }

    public void updateSpecialist(Specialist s) {
        if (s.getId() == null) {
            throw new IllegalArgumentException("ID спеціаліста не може бути порожнім при оновленні");
        }
        validateSpecialist(s);
        try {
            specialistDAO.update(s);
            LOGGER.info("Оновлено дані спеціаліста ID=" + s.getId());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Помилка оновлення спеціаліста ID=" + s.getId(), e);
            throw new RuntimeException("Помилка БД при оновленні спеціаліста: " + e.getMessage());
        }
    }

    public void deleteSpecialist(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID спеціаліста не може бути порожнім при видаленні");
        }
        try {
            specialistDAO.delete(id);
            LOGGER.info("Видалено спеціаліста ID=" + id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Помилка видалення спеціаліста ID=" + id, e);
            throw new RuntimeException("Помилка БД при видаленні спеціаліста: " + e.getMessage());
        }
    }

    private void validateSpecialist(Specialist s) {
        if (s == null) {
            throw new IllegalArgumentException("Дані спеціаліста не можуть бути порожніми");
        }
        if (s.getFullName() == null || s.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("ПІБ спеціаліста є обов'язковим");
        }
        if (s.getSpecialization() == null || s.getSpecialization().trim().isEmpty()) {
            throw new IllegalArgumentException("Спеціалізація є обов'язковою");
        }
        if (s.getPhone() == null || s.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Контактний телефон є обов'язковим");
        }
    }
}