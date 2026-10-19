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
            requestDAO.insert(r);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRequest(Request r) {
        try {
            requestDAO.update(r);
        } catch (SQLException e) {
            e.printStackTrace();
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
                return nextState == RequestState.ПРИЗНАЧЕНА;
            case ПРИЗНАЧЕНА:
                return nextState == RequestState.В_РОБОТІ;
            case В_РОБОТІ:
                return nextState == RequestState.ВИКОНАНО;
            case ВИКОНАНО:
                return false;
            default:
                return false;
        }
    }
}