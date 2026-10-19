package ua.edu.nupp.kpp.server.Servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.edu.nupp.kpp.server.DAO.RequestDAO;
import ua.edu.nupp.kpp.server.Models.Request;
import ua.edu.nupp.kpp.server.Models.RequestState;
import ua.edu.nupp.kpp.server.Service.RequestService;
import ua.edu.nupp.kpp.server.Service.RiskCalculator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/requests")
public class RequestServlet extends HttpServlet {
    private final RequestDAO requestDAO = new RequestDAO();
    private final RequestService requestService = new RequestService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        try {
            String action = req.getParameter("action");
            if ("delete".equals(action)) {
                Long id = Long.parseLong(req.getParameter("id"));
                requestDAO.delete(id);
                resp.getWriter().write("{\"status\":\"success\"}");
                return;
            } else if ("transition".equals(action)) {
                Long id = Long.parseLong(req.getParameter("id"));
                RequestState newState = RequestState.valueOf(req.getParameter("newState"));
                requestService.changeState(id, newState);
                resp.getWriter().write("{\"status\":\"success\"}");
                return;
            }
            objectMapper.writeValue(resp.getWriter(), requestDAO.getAll());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            objectMapper.writeValue(resp.getWriter(), error);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        try {
            String idStr = req.getParameter("id");
            Long roomId = Long.parseLong(req.getParameter("roomId"));
            String specParam = req.getParameter("specialistId");
            Long specialistId = (specParam != null && !specParam.isEmpty()) ? Long.parseLong(specParam) : null;
            String description = req.getParameter("description");

            double f1 = Double.parseDouble(req.getParameter("f1"));
            double f2 = Double.parseDouble(req.getParameter("f2"));
            double f3 = Double.parseDouble(req.getParameter("f3"));

            // Розрахунок інтегрального показника за формулою варіанта №13
            double priorityIndex = RiskCalculator.calculate(f1, f2, f3);

            Request r = new Request();
            r.setRoomId(roomId);
            r.setSpecialistId(specialistId);
            r.setDescription(description);
            r.setF1(f1);
            r.setF2(f2);
            r.setF3(f3);
            r.setPriorityIndex(priorityIndex);

            if (idStr != null && !idStr.isEmpty()) {
                r.setId(Long.parseLong(idStr));
                Request existing = requestDAO.getById(r.getId());
                r.setState(existing != null ? existing.getState() : RequestState.НОВА);
                requestDAO.update(r);
            } else {
                r.setState(RequestState.НОВА); // Нова заявка завжди створюється у стані НОВА
                requestDAO.insert(r);
            }

            resp.getWriter().write("{\"status\":\"success\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            objectMapper.writeValue(resp.getWriter(), error);
        }
    }
}