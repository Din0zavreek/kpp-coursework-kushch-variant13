package ua.edu.nupp.kpp.server.Servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.edu.nupp.kpp.server.Models.Room;
import ua.edu.nupp.kpp.server.Service.RoomService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/rooms")
public class RoomServlet extends HttpServlet {
    private final RoomService roomService = new RoomService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        try {
            String action = req.getParameter("action");
            if ("delete".equals(action)) {
                Long id = Long.parseLong(req.getParameter("id"));
                roomService.deleteRoom(id);
                resp.getWriter().write("{\"status\":\"success\"}");
                return;
            }

            objectMapper.writeValue(resp.getWriter(), roomService.getAllRooms());
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
            String roomNumber = req.getParameter("roomNumber");
            String category = req.getParameter("category");
            int floor = Integer.parseInt(req.getParameter("floor"));

            Room room = new Room();
            room.setRoomNumber(roomNumber);
            room.setCategory(category);
            room.setFloor(floor);

            if (idStr != null && !idStr.isEmpty()) {
                room.setId(Long.parseLong(idStr));
                roomService.updateRoom(room);
            } else {
                roomService.addRoom(room);
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
