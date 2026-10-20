package ua.edu.nupp.kpp.server.Service;

import ua.edu.nupp.kpp.server.DAO.RequestDAO;
import ua.edu.nupp.kpp.server.Models.Request;
import ua.edu.nupp.kpp.server.Models.RequestState;

import java.sql.SQLException;
import java.util.List;

public class RequestService {
    private final RequestDAO requestDAO = new RequestDAO();

    public List<Request> getAllRequests() {
        try {
            return requestDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addRequest(Request r) {
        try {
            if (r.getState() == null || r.getState().isEmpty()) {
                r.setState("НОВА");
            }
            requestDAO.insert(r);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Помилка створення заявки: " + e.getMessage());
        }
    }

    public void updateRequest(Request r) {
        try {
            // Отримуємо стару заявку з бази, щоб перевірити попередній стан
            List<Request> all = requestDAO.getAll();
            Request old = all.stream().filter(req -> req.getId().equals(r.getId())).findFirst().orElse(null);

            if (old != null && old.getState() != null) {
                RequestState currentState = RequestState.valueOf(old.getState());
                RequestState nextState = RequestState.valueOf(r.getState());

                // Якщо стан змінюється, перевіряємо чи дозволений перехід
                if (currentState != nextState) {
                    if (!isValidStateTransition(currentState, nextState)) {
                        throw new IllegalArgumentException("Недопустикре перемикання стану з " + currentState + " на " + nextState);
                    }
                }
            }

            requestDAO.update(r);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Помилка оновлення заявки: " + e.getMessage());
        }
    }

    public void deleteRequest(Long id) {
        try {
            requestDAO.delete(id);
        } catch (SQLException e) {
            e.printStackTrace();
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
}