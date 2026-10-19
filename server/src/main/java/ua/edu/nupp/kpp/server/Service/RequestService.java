package ua.edu.nupp.kpp.server.Service;

import ua.edu.nupp.kpp.server.DAO.RequestDAO;
import ua.edu.nupp.kpp.server.Models.Request;
import ua.edu.nupp.kpp.server.Models.RequestState;

import java.sql.SQLException;
import java.util.logging.Logger;

public class RequestService {
    private static final Logger LOG = Logger.getLogger(RequestService.class.getName());
    private final RequestDAO requestDAO = new RequestDAO();

    public boolean isValidStateTransition(RequestState from, RequestState to) {
        if (from == null || to == null) return false;
        switch (from) {
            case НОВА:
                return to == RequestState.ПРИЗНАЧЕНА;
            case ПРИЗНАЧЕНА:
                return to == RequestState.В_РОБОТІ;
            case В_РОБОТІ:
                return to == RequestState.ВИКОНАНО;
            case ВИКОНАНО:
                return false;
            default:
                return false;
        }
    }

    public void changeState(Long requestId, RequestState newState) throws SQLException {
        Request request = requestDAO.getById(requestId);
        if (request == null) {
            throw new IllegalArgumentException("Заявку з ID " + requestId + " не знайдено");
        }

        if (!isValidStateTransition(request.getState(), newState)) {
            throw new IllegalArgumentException("Недопустимий перехід стану з " + request.getState() + " у " + newState);
        }

        requestDAO.updateState(requestId, newState);
        LOG.info("Змінено стан заявки " + requestId + " на " + newState);
    }
}
