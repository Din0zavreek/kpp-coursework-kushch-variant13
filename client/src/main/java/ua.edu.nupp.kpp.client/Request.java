package ua.edu.nupp.kpp.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Request {
    private Long id;

    @JsonProperty("roomId")
    private Long roomId;

    @JsonProperty("specialistId")
    private Long specialistId;

    @JsonProperty("description")
    private String description;

    @JsonProperty("state")
    private String state;

    @JsonProperty("f1")
    private double f1;

    @JsonProperty("f2")
    private double f2;

    @JsonProperty("f3")
    private double f3;

    @JsonProperty("priorityIndex")
    private double priorityIndex;

    public Request() {}

    public Request(Long id, Long roomId, Long specialistId, String description, String state, double f1, double f2, double f3, double priorityIndex) {
        this.id = id;
        this.roomId = roomId;
        this.specialistId = specialistId;
        this.description = description;
        this.state = state;
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
        this.priorityIndex = priorityIndex;
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

    public double getF1() { return f1; }
    public void setF1(double f1) { this.f1 = f1; }

    public double getF2() { return f2; }
    public void setF2(double f2) { this.f2 = f2; }

    public double getF3() { return f3; }
    public void setF3(double f3) { this.f3 = f3; }

    public double getPriorityIndex() { return priorityIndex; }
    public void setPriorityIndex(double priorityIndex) { this.priorityIndex = priorityIndex; }
}