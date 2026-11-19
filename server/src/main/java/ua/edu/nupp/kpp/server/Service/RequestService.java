package ua.edu.nupp.kpp.server.Service;

import ua.edu.nupp.kpp.server.DAO.RequestDAO;
import ua.edu.nupp.kpp.server.DAO.RoomDAO;
import ua.edu.nupp.kpp.server.DAO.SpecialistDAO;
import ua.edu.nupp.kpp.server.Models.Request;
import ua.edu.nupp.kpp.server.Models.RequestState;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestService {
    private static final Logger LOGGER = Logger.getLogger(RequestService.class.getName());
    private final RequestDAO requestDAO = new RequestDAO();
    private final RoomDAO roomDAO = new RoomDAO();
    private final SpecialistDAO specialistDAO = new SpecialistDAO();

    public List<Request> getAllRequests() {
        try {
            List<Request> list = requestDAO.getAll();
            LOGGER.info("Отримано список заявок. Кількість: " + list.size());
            return list;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Помилка отримання списку заявок", e);
            throw new RuntimeException("Помилка БД при отриманні заявок: " + e.getMessage());
        }
    }

    public void addRequest(Request r) {
        validateRequest(r);
        try {
            validateRelatedEntities(r);
            if (r.getState() == null || r.getState().trim().isEmpty()) {
                r.setState(RequestState.НОВА.name());
            } else {
                RequestState.valueOf(r.getState());
            }
            double priority = PriorityCalculator.calculate(r.getF1(), r.getF2(), r.getF3());
            r.setPriorityIndex(priority);

            requestDAO.insert(r);
            LOGGER.info("Створено заявку для кімнати ID=" + r.getRoomId() + ", стан=" + r.getState() + ", пріоритет=" + priority);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Помилка валідації при створенні заявки: " + e.getMessage());
            throw e;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Помилка БД при створенні заявки", e);
            throw new RuntimeException("Помилка створення заявки: " + e.getMessage());
        }
    }

    public void updateRequest(Request r) {
        if (r.getId() == null) {
            throw new IllegalArgumentException("ID заявки не може бути порожнім при оновленні");
        }
        validateRequest(r);
        try {
            validateRelatedEntities(r);
            List<Request> all = requestDAO.getAll();
            Request old = all.stream().filter(req -> req.getId().equals(r.getId())).findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Заявку з ID=" + r.getId() + " не знайдено"));

            if (old.getState() != null && r.getState() != null) {
                RequestState currentState = RequestState.valueOf(old.getState());
                RequestState nextState = RequestState.valueOf(r.getState());

                if (currentState != nextState && !isValidStateTransition(currentState, nextState)) {
                    throw new IllegalArgumentException("Недопустиме перемикання стану з " + currentState + " на " + nextState);
                }
            }

            double priority = PriorityCalculator.calculate(r.getF1(), r.getF2(), r.getF3());
            r.setPriorityIndex(priority);

            requestDAO.update(r);
            LOGGER.info("Оновлено заявку ID=" + r.getId() + ", новий стан=" + r.getState() + ", пріоритет=" + priority);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Помилка валідації при оновленні заявки: " + e.getMessage());
            throw e;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Помилка БД при оновленні заявки ID=" + r.getId(), e);
            throw new RuntimeException("Помилка оновлення заявки: " + e.getMessage());
        }
    }

    public void deleteRequest(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID заявки не може бути порожнім при видаленні");
        }
        try {
            requestDAO.delete(id);
            LOGGER.info("Видалено заявку ID=" + id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Помилка видалення заявки ID=" + id, e);
            throw new RuntimeException("Помилка видалення заявки: " + e.getMessage());
        }
    }

    public boolean isValidStateTransition(RequestState currentState, RequestState nextState) {
        if (currentState == null || nextState == null) {
            return false;
        }
        switch (currentState) {
            case НОВА:
                return nextState == RequestState.ПРИЗНАЧЕНА || nextState == RequestState.НОВА;
            case ПРИЗНАЧЕНА:
                return nextState == RequestState.В_РОБОТІ || nextState == RequestState.ПРИЗНАЧЕНА;
            case В_РОБОТІ:
                return nextState == RequestState.ВИКОНАНО || nextState == RequestState.В_РОБОТІ;
            case ВИКОНАНО:
                return nextState == RequestState.ВИКОНАНО;
            default:
                return false;
        }
    }

    private void validateRequest(Request r) {
        if (r == null) {
            throw new IllegalArgumentException("Дані заявки не можуть бути порожніми");
        }
        if (r.getRoomId() == null) {
            throw new IllegalArgumentException("Не вказано ID кімнати");
        }
        if (r.getDescription() == null || r.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Опис несправності є обов'язковим");
        }
    }

    private void validateRelatedEntities(Request r) throws SQLException {
        boolean roomExists = roomDAO.getAll().stream().anyMatch(room -> room.getId().equals(r.getRoomId()));
        if (!roomExists) {
            throw new IllegalArgumentException("Кімнату з ID=" + r.getRoomId() + " не знайдено в базі даних");
        }
        if (r.getSpecialistId() != null) {
            boolean specExists = specialistDAO.getAll().stream().anyMatch(s -> s.getId().equals(r.getSpecialistId()));
            if (!specExists) {
                throw new IllegalArgumentException("Спеціаліста з ID=" + r.getSpecialistId() + " не знайдено в базі даних");
            }
        }
    }
}