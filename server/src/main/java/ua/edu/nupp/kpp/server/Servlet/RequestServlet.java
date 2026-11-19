package ua.edu.nupp.kpp.server.Servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.edu.nupp.kpp.server.Models.Request;
import ua.edu.nupp.kpp.server.Service.RequestService;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

public class RequestServlet extends HttpServlet {
    private final RequestService service = new RequestService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            if ("delete".equals(req.getParameter("action"))) {
                service.deleteRequest(Long.parseLong(req.getParameter("id")));
                writeJson(resp, Map.of("status", "success"));
                return;
            }
            writeJson(resp, service.getAllRequests());
        } catch (Exception e) {
            writeError(resp, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            if ("delete".equals(req.getParameter("action"))) {
                service.deleteRequest(Long.parseLong(req.getParameter("id")));
                writeJson(resp, Map.of("status", "success"));
                return;
            }
            String idStr = req.getParameter("id");
            String roomStr = req.getParameter("roomId");
            if (roomStr == null || roomStr.isBlank()) {
                throw new IllegalArgumentException("ID кімнати є обов'язковим");
            }
            String specStr = req.getParameter("specialistId");

            Request r = new Request();
            r.setRoomId(Long.parseLong(roomStr));
            r.setSpecialistId((specStr != null && !specStr.isEmpty() && !"null".equals(specStr)) ? Long.parseLong(specStr) : null);
            r.setDescription(req.getParameter("description"));
            r.setState(req.getParameter("state"));

            double f1 = req.getParameter("f1") != null && !req.getParameter("f1").isEmpty() ? Double.parseDouble(req.getParameter("f1")) : 70.0;
            double f2 = req.getParameter("f2") != null && !req.getParameter("f2").isEmpty() ? Double.parseDouble(req.getParameter("f2")) : 60.0;
            double f3 = req.getParameter("f3") != null && !req.getParameter("f3").isEmpty() ? Double.parseDouble(req.getParameter("f3")) : 50.0;
            r.setF1(f1);
            r.setF2(f2);
            r.setF3(f3);

            if (idStr != null && !idStr.isEmpty() && !"null".equals(idStr)) {
                r.setId(Long.parseLong(idStr));
                service.updateRequest(r);
            } else {
                service.addRequest(r);
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