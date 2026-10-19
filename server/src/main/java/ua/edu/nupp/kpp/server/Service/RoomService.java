package ua.edu.nupp.kpp.server.Service;

import ua.edu.nupp.kpp.server.DAO.RoomDAO;
import ua.edu.nupp.kpp.server.Models.Room;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class RoomService {
    private static final Logger LOG = Logger.getLogger(RoomService.class.getName());
    private final RoomDAO roomDAO = new RoomDAO();

    public List<Room> getAllRooms() throws SQLException {
        return roomDAO.getAll();
    }

    public Room getRoomById(Long id) throws SQLException {
        return roomDAO.getById(id);
    }

    public void addRoom(Room room) throws SQLException {
        validateRoom(room);
        roomDAO.insert(room);
        LOG.info("Додано новий номер готелю: " + room.getRoomNumber());
    }

    public void updateRoom(Room room) throws SQLException {
        if (room.getId() == null) {
            throw new IllegalArgumentException("ID номера не може бути порожнім для оновлення");
        }
        validateRoom(room);
        roomDAO.update(room);
        LOG.info("Оновлено дані номера ID: " + room.getId());
    }

    public void deleteRoom(Long id) throws SQLException {
        roomDAO.delete(id);
        LOG.info("Видалено номер готелю з ID: " + id);
    }

    private void validateRoom(Room room) {
        if (room.getRoomNumber() == null || room.getRoomNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Номер кімнати не може бути порожнім");
        }
        if (room.getCategory() == null || room.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Категорія номера не може бути порожньою");
        }
        if (room.getFloor() <= 0) {
            throw new IllegalArgumentException("Поверх має бути більшим за 0");
        }
    }
}
