package ua.edu.nupp.kpp.server.Servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.edu.nupp.kpp.server.Models.Request;
import ua.edu.nupp.kpp.server.Service.RequestService;
import ua.edu.nupp.kpp.server.Service.RiskCalculator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/requests")
public class RequestServlet extends HttpServlet {
    private final RequestService service = new RequestService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        try {
            if ("delete".equals(req.getParameter("action"))) {
                service.deleteRequest(Long.parseLong(req.getParameter("id")));
                resp.getWriter().write("{\"status\":\"success\"}");
                return;
            }
            mapper.writeValue(resp.getWriter(), service.getAllRequests());
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
            String specStr = req.getParameter("specialistId");

            Request r = new Request();
            r.setRoomId(Long.parseLong(req.getParameter("roomId")));
            r.setSpecialistId((specStr != null && !specStr.isEmpty()) ? Long.parseLong(specStr) : null);
            r.setDescription(req.getParameter("description"));
            r.setState(req.getParameter("state"));

            double f1 = req.getParameter("f1") != null && !req.getParameter("f1").isEmpty() ? Double.parseDouble(req.getParameter("f1")) : 70.0;
            double f2 = req.getParameter("f2") != null && !req.getParameter("f2").isEmpty() ? Double.parseDouble(req.getParameter("f2")) : 60.0;
            double f3 = req.getParameter("f3") != null && !req.getParameter("f3").isEmpty() ? Double.parseDouble(req.getParameter("f3")) : 50.0;
            r.setF1(f1);
            r.setF2(f2);
            r.setF3(f3);
            r.setPriorityIndex(RiskCalculator.calculate(f1, f2, f3));

            if (idStr != null && !idStr.isEmpty() && !"null".equals(idStr)) {
                r.setId(Long.parseLong(idStr));
                service.updateRequest(r);
            } else {
                service.addRequest(r);
            }
            resp.getWriter().write("{\"status\":\"success\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> err = new HashMap<>(); err.put("error", e.getMessage());
            mapper.writeValue(resp.getWriter(), err);
        }
    }
}