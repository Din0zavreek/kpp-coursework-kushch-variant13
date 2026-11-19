package ua.edu.nupp.kpp.server.Servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.edu.nupp.kpp.server.Models.Room;
import ua.edu.nupp.kpp.server.Service.RoomService;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

public class RoomServlet extends HttpServlet {
    private final RoomService roomService = new RoomService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            if ("delete".equals(req.getParameter("action"))) {
                roomService.deleteRoom(Long.parseLong(req.getParameter("id")));
                writeJson(resp, Map.of("status", "success"));
                return;
            }
            writeJson(resp, roomService.getAllRooms());
        } catch (Exception e) {
            writeError(resp, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            if ("delete".equals(req.getParameter("action"))) {
                roomService.deleteRoom(Long.parseLong(req.getParameter("id")));
                writeJson(resp, Map.of("status", "success"));
                return;
            }
            String idStr = req.getParameter("id");
            Room room = new Room();
            room.setRoomNumber(req.getParameter("roomNumber"));
            room.setCategory(req.getParameter("category"));
            String floorStr = req.getParameter("floor");
            if (floorStr == null || floorStr.isBlank()) {
                throw new IllegalArgumentException("Поверх є обов'язковим полем");
            }
            room.setFloor(Integer.parseInt(floorStr));

            if (idStr != null && !idStr.isEmpty() && !"null".equals(idStr)) {
                room.setId(Long.parseLong(idStr));
                roomService.updateRoom(room);
            } else {
                roomService.addRoom(room);
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