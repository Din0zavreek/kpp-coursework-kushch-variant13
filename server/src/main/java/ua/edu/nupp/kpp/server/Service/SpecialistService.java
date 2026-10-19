package ua.edu.nupp.kpp.server.Service;

import ua.edu.nupp.kpp.server.DAO.SpecialistDAO;
import ua.edu.nupp.kpp.server.Models.Specialist;
import java.sql.SQLException;
import java.util.List;

public class SpecialistService {
    private final SpecialistDAO specialistDAO = new SpecialistDAO();

    public List<Specialist> getAllSpecialists() {
        try { return specialistDAO.getAll(); } catch (SQLException e) { e.printStackTrace(); return null; }
    }
    public void addSpecialist(Specialist s) {
        try { specialistDAO.insert(s); } catch (SQLException e) { e.printStackTrace(); }
    }
    public void updateSpecialist(Specialist s) {
        try { specialistDAO.update(s); } catch (SQLException e) { e.printStackTrace(); }
    }
    public void deleteSpecialist(Long id) {
        try { specialistDAO.delete(id); } catch (SQLException e) { e.printStackTrace(); }
    }
}