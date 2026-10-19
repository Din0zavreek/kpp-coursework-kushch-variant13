package ua.edu.nupp.kpp.server.Servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.edu.nupp.kpp.server.DAO.ExecutionRecordDAO;
import ua.edu.nupp.kpp.server.Models.ExecutionRecord;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/execution-records")
public class ExecutionRecordServlet extends HttpServlet {
    private final ExecutionRecordDAO executionRecordDAO = new ExecutionRecordDAO();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        try {
            String action = req.getParameter("action");
            if ("delete".equals(action)) {
                Long id = Long.parseLong(req.getParameter("id"));
                executionRecordDAO.delete(id);
                resp.getWriter().write("{\"status\":\"success\"}");
                return;
            }
            objectMapper.writeValue(resp.getWriter(), executionRecordDAO.getAll());
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
            Long requestId = Long.parseLong(req.getParameter("requestId"));
            String reportDetails = req.getParameter("reportDetails");
            String completedDate = req.getParameter("completedDate");

            ExecutionRecord er = new ExecutionRecord();
            er.setRequestId(requestId);
            er.setReportDetails(reportDetails);
            er.setCompletedDate(completedDate);

            if (idStr != null && !idStr.isEmpty()) {
                er.setId(Long.parseLong(idStr));
                executionRecordDAO.update(er);
            } else {
                executionRecordDAO.insert(er);
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
