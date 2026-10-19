package ua.edu.nupp.kpp.server.Servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.edu.nupp.kpp.server.Models.ExecutionRecord;
import ua.edu.nupp.kpp.server.Service.ExecutionRecordService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/execution-records")
public class ExecutionRecordServlet extends HttpServlet {
    private final ExecutionRecordService service = new ExecutionRecordService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        try {
            if ("delete".equals(req.getParameter("action"))) {
                service.deleteRecord(Long.parseLong(req.getParameter("id")));
                resp.getWriter().write("{\"status\":\"success\"}");
                return;
            }
            mapper.writeValue(resp.getWriter(), service.getAllRecords());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> err = new HashMap<>(); err.put("error", e.getMessage());
            mapper.writeValue(resp.getWriter(), err);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8"); resp.setContentType("application/json; charset=UTF-8");
        try {
            String idStr = req.getParameter("id");
            ExecutionRecord rec = new ExecutionRecord();
            rec.setRequestId(Long.parseLong(req.getParameter("requestId")));
            rec.setExecutionDate(req.getParameter("executionDate"));
            rec.setResultNotes(req.getParameter("resultNotes"));

            if (idStr != null && !idStr.isEmpty()) {
                rec.setId(Long.parseLong(idStr));
                service.updateRecord(rec);
            } else {
                service.addRecord(rec);
            }
            resp.getWriter().write("{\"status\":\"success\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> err = new HashMap<>(); err.put("error", e.getMessage());
            mapper.writeValue(resp.getWriter(), err);
        }
    }
}