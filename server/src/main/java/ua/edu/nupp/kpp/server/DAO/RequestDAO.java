package ua.edu.nupp.kpp.server.DAO;

import ua.edu.nupp.kpp.server.DBUtil;
import ua.edu.nupp.kpp.server.Models.Request;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestDAO {
    public List<Request> getAll() throws SQLException {
        List<Request> list = new ArrayList<>();
        String sql = "SELECT id, room_id, specialist_id, description, state FROM request";
        try (Connection c = DBUtil.getConnection(); Statement stmt = c.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Request(
                        rs.getLong("id"),
                        rs.getLong("room_id"),
                        rs.getObject("specialist_id") != null ? rs.getLong("specialist_id") : null,
                        rs.getString("description"),
                        rs.getString("state")
                ));
            }
        }
        return list;
    }

    public void insert(Request r) throws SQLException {
        String sql = "INSERT INTO request (room_id, specialist_id, description, state) VALUES (?, ?, ?, ?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, r.getRoomId());
            if (r.getSpecialistId() != null) p.setLong(2, r.getSpecialistId()); else p.setNull(2, Types.BIGINT);
            p.setString(3, r.getDescription());
            p.setString(4, r.getState());
            p.executeUpdate();
        }
    }

    public void update(Request r) throws SQLException {
        String sql = "UPDATE request SET room_id = ?, specialist_id = ?, description = ?, state = ? WHERE id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, r.getRoomId());
            if (r.getSpecialistId() != null) p.setLong(2, r.getSpecialistId()); else p.setNull(2, Types.BIGINT);
            p.setString(3, r.getDescription());
            p.setString(4, r.getState());
            p.setLong(5, r.getId());
            p.executeUpdate();
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM request WHERE id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            p.executeUpdate();
        }
    }
}