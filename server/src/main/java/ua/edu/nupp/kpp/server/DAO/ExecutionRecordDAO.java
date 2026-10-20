package ua.edu.nupp.kpp.server.DAO;

import ua.edu.nupp.kpp.server.DBUtil;
import ua.edu.nupp.kpp.server.Models.ExecutionRecord;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExecutionRecordDAO {
    public List<ExecutionRecord> getAll() throws SQLException {
        List<ExecutionRecord> list = new ArrayList<>();
        String sql = "SELECT id, request_id, completed_date, report_details FROM execution_record";
        try (Connection c = DBUtil.getConnection(); Statement stmt = c.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new ExecutionRecord(
                        rs.getLong("id"),
                        rs.getLong("request_id"),
                        rs.getString("completed_date"),
                        rs.getString("report_details")
                ));
            }
        }
        return list;
    }

    public void insert(ExecutionRecord rec) throws SQLException {
        String sql = "INSERT INTO execution_record (request_id, completed_date, report_details) VALUES (?, ?, ?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, rec.getRequestId());
            p.setString(2, rec.getExecutionDate());
            p.setString(3, rec.getResultNotes());
            p.executeUpdate();
        }
    }

    public void update(ExecutionRecord rec) throws SQLException {
        String sql = "UPDATE execution_record SET request_id = ?, completed_date = ?, report_details = ? WHERE id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, rec.getRequestId());
            p.setString(2, rec.getExecutionDate());
            p.setString(3, rec.getResultNotes());
            p.setLong(4, rec.getId());
            p.executeUpdate();
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM execution_record WHERE id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            p.executeUpdate();
        }
    }
}