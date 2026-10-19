package ua.edu.nupp.kpp.server.DAO;

import ua.edu.nupp.kpp.server.DBUtil;
import ua.edu.nupp.kpp.server.Models.Room;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    public List<Room> getAll() throws SQLException {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT id, room_number, category, floor FROM room";
        try (Connection c = DBUtil.getConnection(); Statement stmt = c.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Room(rs.getLong("id"), rs.getString("room_number"), rs.getString("category"), rs.getInt("floor")));
            }
        }
        return list;
    }

    public void insert(Room r) throws SQLException {
        String sql = "INSERT INTO room (room_number, category, floor) VALUES (?, ?, ?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, r.getRoomNumber());
            p.setString(2, r.getCategory());
            p.setInt(3, r.getFloor());
            p.executeUpdate();
        }
    }

    public void update(Room r) throws SQLException {
        String sql = "UPDATE room SET room_number = ?, category = ?, floor = ? WHERE id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, r.getRoomNumber());
            p.setString(2, r.getCategory());
            p.setInt(3, r.getFloor());
            p.setLong(4, r.getId());
            p.executeUpdate();
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM room WHERE id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            p.executeUpdate();
        }
    }
}