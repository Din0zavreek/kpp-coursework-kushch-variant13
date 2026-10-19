package ua.edu.nupp.kpp.server.Models;

public class Request {
    private Long id;
    private Long roomId;
    private Long specialistId;
    private String description;
    private String state;

    public Request() {}

    public Request(Long id, Long roomId, Long specialistId, String description, String state) {
        this.id = id;
        this.roomId = roomId;
        this.specialistId = specialistId;
        this.description = description;
        this.state = state;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    public Long getSpecialistId() { return specialistId; }
    public void setSpecialistId(Long specialistId) { this.specialistId = specialistId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
}