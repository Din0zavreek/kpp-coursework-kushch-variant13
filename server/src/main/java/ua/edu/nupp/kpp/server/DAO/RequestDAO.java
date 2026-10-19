package ua.edu.nupp.kpp.server.DAO;

import ua.edu.nupp.kpp.server.DBUtil;
import ua.edu.nupp.kpp.server.Models.Request;
import ua.edu.nupp.kpp.server.Models.RequestState;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestDAO {

    public List<Request> getAll() throws SQLException {
        List<Request> list = new ArrayList<>();
        String sql = "SELECT id, room_id, specialist_id, description, state, f1, f2, f3, priority_index FROM request";
        try (Connection c = DBUtil.getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Request(
                        rs.getLong("id"),
                        rs.getLong("room_id"),
                        rs.getObject("specialist_id") != null ? rs.getLong("specialist_id") : null,
                        rs.getString("description"),
                        RequestState.valueOf(rs.getString("state")),
                        rs.getDouble("f1"),
                        rs.getDouble("f2"),
                        rs.getDouble("f3"),
                        rs.getDouble("priority_index")
                ));
            }
        }
        return list;
    }

    public Request getById(Long id) throws SQLException {
        String sql = "SELECT id, room_id, specialist_id, description, state, f1, f2, f3, priority_index FROM request WHERE id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return new Request(
                            rs.getLong("id"),
                            rs.getLong("room_id"),
                            rs.getObject("specialist_id") != null ? rs.getLong("specialist_id") : null,
                            rs.getString("description"),
                            RequestState.valueOf(rs.getString("state")),
                            rs.getDouble("f1"),
                            rs.getDouble("f2"),
                            rs.getDouble("f3"),
                            rs.getDouble("priority_index")
                    );
                }
            }
        }
        return null;
    }

    public void insert(Request r) throws SQLException {
        String sql = "INSERT INTO request (room_id, specialist_id, description, state, f1, f2, f3, priority_index) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, r.getRoomId());
            if (r.getSpecialistId() != null) {
                p.setLong(2, r.getSpecialistId());
            } else {
                p.setNull(2, Types.BIGINT);
            }
            p.setString(3, r.getDescription());
            p.setString(4, r.getState().name());
            p.setDouble(5, r.getF1());
            p.setDouble(6, r.getF2());
            p.setDouble(7, r.getF3());
            p.setDouble(8, r.getPriorityIndex());
            p.executeUpdate();
        }
    }

    public void update(Request r) throws SQLException {
        String sql = "UPDATE request SET room_id = ?, specialist_id = ?, description = ?, state = ?, f1 = ?, f2 = ?, f3 = ?, priority_index = ? WHERE id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, r.getRoomId());
            if (r.getSpecialistId() != null) {
                p.setLong(2, r.getSpecialistId());
            } else {
                p.setNull(2, Types.BIGINT);
            }
            p.setString(3, r.getDescription());
            p.setString(4, r.getState().name());
            p.setDouble(5, r.getF1());
            p.setDouble(6, r.getF2());
            p.setDouble(7, r.getF3());
            p.setDouble(8, r.getPriorityIndex());
            p.setLong(9, r.getId());
            p.executeUpdate();
        }
    }

    public void updateState(Long id, RequestState newState) throws SQLException {
        String sql = "UPDATE request SET state = ? WHERE id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, newState.name());
            p.setLong(2, id);
            p.executeUpdate();
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM request WHERE id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            p.executeUpdate();
        }
    }
}