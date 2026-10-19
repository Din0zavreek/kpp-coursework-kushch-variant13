package ua.edu.nupp.kpp.server.Servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.edu.nupp.kpp.server.Models.Specialist;
import ua.edu.nupp.kpp.server.Service.SpecialistService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/specialists")
public class SpecialistServlet extends HttpServlet {
    private final SpecialistService service = new SpecialistService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        try {
            if ("delete".equals(req.getParameter("action"))) {
                service.deleteSpecialist(Long.parseLong(req.getParameter("id")));
                resp.getWriter().write("{\"status\":\"success\"}");
                return;
            }
            mapper.writeValue(resp.getWriter(), service.getAllSpecialists());
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
            Specialist s = new Specialist();
            s.setFullName(req.getParameter("fullName"));
            s.setSpecialization(req.getParameter("specialization"));
            s.setPhone(req.getParameter("phone"));

            if (idStr != null && !idStr.isEmpty()) {
                s.setId(Long.parseLong(idStr));
                service.updateSpecialist(s);
            } else {
                service.addSpecialist(s);
            }
            resp.getWriter().write("{\"status\":\"success\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> err = new HashMap<>(); err.put("error", e.getMessage());
            mapper.writeValue(resp.getWriter(), err);
        }
    }
}