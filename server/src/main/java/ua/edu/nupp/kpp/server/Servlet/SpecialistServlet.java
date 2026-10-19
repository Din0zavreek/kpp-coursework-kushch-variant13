package ua.edu.nupp.kpp.server.Servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.edu.nupp.kpp.server.DAO.SpecialistDAO;
import ua.edu.nupp.kpp.server.Models.Specialist;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/specialists")
public class SpecialistServlet extends HttpServlet {
    private final SpecialistDAO specialistDAO = new SpecialistDAO();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        try {
            String action = req.getParameter("action");
            if ("delete".equals(action)) {
                Long id = Long.parseLong(req.getParameter("id"));
                specialistDAO.delete(id);
                resp.getWriter().write("{\"status\":\"success\"}");
                return;
            }
            objectMapper.writeValue(resp.getWriter(), specialistDAO.getAll());
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
            String fullName = req.getParameter("fullName");
            String specialization = req.getParameter("specialization");
            String phone = req.getParameter("phone");

            Specialist s = new Specialist();
            s.setFullName(fullName);
            s.setSpecialization(specialization);
            s.setPhone(phone);

            if (idStr != null && !idStr.isEmpty()) {
                s.setId(Long.parseLong(idStr));
                specialistDAO.update(s);
            } else {
                specialistDAO.insert(s);
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