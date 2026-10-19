package ua.edu.nupp.kpp.server.Service;

import ua.edu.nupp.kpp.server.DAO.RoomDAO;
import ua.edu.nupp.kpp.server.Models.Room;
import java.sql.SQLException;
import java.util.List;

public class RoomService {
    private final RoomDAO roomDAO = new RoomDAO();

    public List<Room> getAllRooms() {
        try { return roomDAO.getAll(); } catch (SQLException e) { e.printStackTrace(); return null; }
    }
    public void addRoom(Room room) {
        try { roomDAO.insert(room); } catch (SQLException e) { e.printStackTrace(); }
    }
    public void updateRoom(Room room) {
        try { roomDAO.update(room); } catch (SQLException e) { e.printStackTrace(); }
    }
    public void deleteRoom(Long id) {
        try { roomDAO.delete(id); } catch (SQLException e) { e.printStackTrace(); }
    }
}
