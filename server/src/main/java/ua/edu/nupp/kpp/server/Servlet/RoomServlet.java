package ua.edu.nupp.kpp.server.Servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.edu.nupp.kpp.server.Models.Room;
import ua.edu.nupp.kpp.server.Service.RoomService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/rooms")
public class RoomServlet extends HttpServlet {
    private final RoomService roomService = new RoomService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        try {
            if ("delete".equals(req.getParameter("action"))) {
                roomService.deleteRoom(Long.parseLong(req.getParameter("id")));
                resp.getWriter().write("{\"status\":\"success\"}");
                return;
            }
            mapper.writeValue(resp.getWriter(), roomService.getAllRooms());
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
            Room room = new Room();
            room.setRoomNumber(req.getParameter("roomNumber"));
            room.setCategory(req.getParameter("category"));
            room.setFloor(Integer.parseInt(req.getParameter("floor")));

            if (idStr != null && !idStr.isEmpty()) {
                room.setId(Long.parseLong(idStr));
                roomService.updateRoom(room);
            } else {
                roomService.addRoom(room);
            }
            resp.getWriter().write("{\"status\":\"success\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> err = new HashMap<>(); err.put("error", e.getMessage());
            mapper.writeValue(resp.getWriter(), err);
        }
    }
}