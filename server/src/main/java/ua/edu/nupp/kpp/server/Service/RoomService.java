package ua.edu.nupp.kpp.server.Service;

import ua.edu.nupp.kpp.server.DAO.RoomDAO;
import ua.edu.nupp.kpp.server.Models.Room;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoomService {
    private static final Logger LOGGER = Logger.getLogger(RoomService.class.getName());
    private final RoomDAO roomDAO = new RoomDAO();

    public List<Room> getAllRooms() {
        try {
            List<Room> rooms = roomDAO.getAll();
            LOGGER.info("Fetched rooms list. Count: " + rooms.size());
            return rooms;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error while fetching rooms", e);
            throw new RuntimeException("Помилка БД при отриманні номерів: " + e.getMessage());
        }
    }

    public void addRoom(Room room) {
        validateRoom(room);
        try {
            roomDAO.insert(room);
            LOGGER.info("Added new room: " + room.getRoomNumber() + " (floor " + room.getFloor() + ")");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error while adding room", e);
            throw new RuntimeException("Помилка БД при додаванні номера: " + e.getMessage());
        }
    }

    public void updateRoom(Room room) {
        if (room.getId() == null) {
            throw new IllegalArgumentException("ID номера не може бути порожнім при оновленні");
        }
        validateRoom(room);
        try {
            roomDAO.update(room);
            LOGGER.info("Updated room ID=" + room.getId());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error while updating room ID=" + room.getId(), e);
            throw new RuntimeException("Помилка БД при оновленні номера: " + e.getMessage());
        }
    }

    public void deleteRoom(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID номера не може бути порожнім при видаленні");
        }
        try {
            roomDAO.delete(id);
            LOGGER.info("Deleted room ID=" + id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error while deleting room ID=" + id, e);
            throw new RuntimeException("Помилка БД при видаленні номера: " + e.getMessage());
        }
    }

    public static void validateRoom(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Дані номера не можуть бути порожніми");
        }
        if (room.getRoomNumber() == null || room.getRoomNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Номер кімнати є обов'язковим полем");
        }
        if (room.getCategory() == null || room.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Категорія номера є обов'язковою");
        }
        if (room.getFloor() <= 0) {
            throw new IllegalArgumentException("Поверх повинен бути більшим за 0");
        }
    }
}