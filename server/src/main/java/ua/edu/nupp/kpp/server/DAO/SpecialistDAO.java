package ua.edu.nupp.kpp.server.DAO;

import ua.edu.nupp.kpp.server.DBUtil;
import ua.edu.nupp.kpp.server.Models.Specialist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpecialistDAO {

    public List<Specialist> getAll() throws SQLException {
        List<Specialist> list = new ArrayList<>();
        String sql = "SELECT id, full_name, specialization, phone FROM specialist";
        try (Connection c = DBUtil.getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Specialist(
                        rs.getLong("id"),
                        rs.getString("full_name"),
                        rs.getString("specialization"),
                        rs.getString("phone")
                ));
            }
        }
        return list;
    }

    public Specialist getById(Long id) throws SQLException {
        String sql = "SELECT id, full_name, specialization, phone FROM specialist WHERE id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return new Specialist(
                            rs.getLong("id"),
                            rs.getString("full_name"),
                            rs.getString("specialization"),
                            rs.getString("phone")
                    );
                }
            }
        }
        return null;
    }

    public void insert(Specialist s) throws SQLException {
        String sql = "INSERT INTO specialist (full_name, specialization, phone) VALUES (?, ?, ?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, s.getFullName());
            p.setString(2, s.getSpecialization());
            p.setString(3, s.getPhone());
            p.executeUpdate();
        }
    }

    public void update(Specialist s) throws SQLException {
        String sql = "UPDATE specialist SET full_name = ?, specialization = ?, phone = ? WHERE id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, s.getFullName());
            p.setString(2, s.getSpecialization());
            p.setString(3, s.getPhone());
            p.setLong(4, s.getId());
            p.executeUpdate();
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM specialist WHERE id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            p.executeUpdate();
        }
    }
}