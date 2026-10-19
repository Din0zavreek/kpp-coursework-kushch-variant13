package ua.edu.nupp.kpp.server.Models;

public class Specialist {
    private Long id;
    private String fullName;
    private String specialization;
    private String phone;

    public Specialist() {}

    public Specialist(Long id, String fullName, String specialization, String phone) {
        this.id = id;
        this.fullName = fullName;
        this.specialization = specialization;
        this.phone = phone;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}