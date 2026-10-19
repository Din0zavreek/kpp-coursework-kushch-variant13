package ua.edu.nupp.kpp.server.Service;

import ua.edu.nupp.kpp.server.DAO.ExecutionRecordDAO;
import ua.edu.nupp.kpp.server.Models.ExecutionRecord;
import java.sql.SQLException;
import java.util.List;

public class ExecutionRecordService {
    private final ExecutionRecordDAO recordDAO = new ExecutionRecordDAO();

    public List<ExecutionRecord> getAllRecords() {
        try { return recordDAO.getAll(); } catch (SQLException e) { e.printStackTrace(); return null; }
    }
    public void addRecord(ExecutionRecord rec) {
        try { recordDAO.insert(rec); } catch (SQLException e) { e.printStackTrace(); }
    }
    public void updateRecord(ExecutionRecord rec) {
        try { recordDAO.update(rec); } catch (SQLException e) { e.printStackTrace(); }
    }
    public void deleteRecord(Long id) {
        try { recordDAO.delete(id); } catch (SQLException e) { e.printStackTrace(); }
    }
}