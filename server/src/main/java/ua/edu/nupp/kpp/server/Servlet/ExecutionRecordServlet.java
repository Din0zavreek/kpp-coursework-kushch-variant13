package ua.edu.nupp.kpp.server.Servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.edu.nupp.kpp.server.Models.ExecutionRecord;
import ua.edu.nupp.kpp.server.Service.ExecutionRecordService;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

public class ExecutionRecordServlet extends HttpServlet {
    private final ExecutionRecordService service = new ExecutionRecordService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            if ("delete".equals(req.getParameter("action"))) {
                service.deleteRecord(Long.parseLong(req.getParameter("id")));
                writeJson(resp, Map.of("status", "success"));
                return;
            }
            writeJson(resp, service.getAllRecords());
        } catch (Exception e) {
            writeError(resp, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            if ("delete".equals(req.getParameter("action"))) {
                service.deleteRecord(Long.parseLong(req.getParameter("id")));
                writeJson(resp, Map.of("status", "success"));
                return;
            }
            String idStr = req.getParameter("id");
            String reqIdStr = req.getParameter("requestId");
            if (reqIdStr == null || reqIdStr.isBlank()) {
                throw new IllegalArgumentException("ID заявки є обов'язковим");
            }
            ExecutionRecord rec = new ExecutionRecord();
            rec.setRequestId(Long.parseLong(reqIdStr));
            rec.setExecutionDate(req.getParameter("completedDate"));
            rec.setResultNotes(req.getParameter("reportDetails"));

            if (idStr != null && !idStr.isEmpty() && !"null".equals(idStr)) {
                rec.setId(Long.parseLong(idStr));
                service.updateRecord(rec);
            } else {
                service.addRecord(rec);
            }
            writeJson(resp, Map.of("status", "success"));
        } catch (Exception e) {
            writeError(resp, e);
        }
    }

    private void writeJson(HttpServletResponse resp, Object data) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        mapper.writeValue(resp.getWriter(), data);
    }

    private void writeError(HttpServletResponse resp, Exception e) throws IOException {
        boolean clientError = e instanceof IllegalArgumentException;
        int status = clientError ? HttpServletResponse.SC_BAD_REQUEST : HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        String message = clientError
                ? (e.getMessage() == null ? "Некоректний запит" : e.getMessage())
                : "Внутрішня помилка сервера";
        resp.setStatus(status);
        writeJson(resp, Map.of("error", message));
    }
}