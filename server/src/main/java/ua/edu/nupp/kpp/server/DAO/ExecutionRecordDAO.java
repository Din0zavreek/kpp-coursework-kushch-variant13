package ua.edu.nupp.kpp.server.DAO;

import ua.edu.nupp.kpp.server.DBUtil;
import ua.edu.nupp.kpp.server.Models.ExecutionRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExecutionRecordDAO {

    public List<ExecutionRecord> getAll() throws SQLException {
        List<ExecutionRecord> list = new ArrayList<>();
        String sql = "SELECT id, request_id, report_details, completed_date FROM execution_record";
        try (Connection c = DBUtil.getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new ExecutionRecord(
                        rs.getLong("id"),
                        rs.getLong("request_id"),
                        rs.getString("report_details"),
                        rs.getString("completed_date")
                ));
            }
        }
        return list;
    }

    public ExecutionRecord getById(Long id) throws SQLException {
        String sql = "SELECT id, request_id, report_details, completed_date FROM execution_record WHERE id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return new ExecutionRecord(
                            rs.getLong("id"),
                            rs.getLong("request_id"),
                            rs.getString("report_details"),
                            rs.getString("completed_date")
                    );
                }
            }
        }
        return null;
    }

    public void insert(ExecutionRecord er) throws SQLException {
        String sql = "INSERT INTO execution_record (request_id, report_details, completed_date) VALUES (?, ?, ?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, er.getRequestId());
            p.setString(2, er.getReportDetails());
            p.setString(3, er.getCompletedDate());
            p.executeUpdate();
        }
    }

    public void update(ExecutionRecord er) throws SQLException {
        String sql = "UPDATE execution_record SET request_id = ?, report_details = ?, completed_date = ? WHERE id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, er.getRequestId());
            p.setString(2, er.getReportDetails());
            p.setString(3, er.getCompletedDate());
            p.setLong(4, er.getId());
            p.executeUpdate();
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM execution_record WHERE id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            p.executeUpdate();
        }
    }
}