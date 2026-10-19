package ua.edu.nupp.kpp.client;

public class Room {
    private Long id;
    private String roomNumber;
    private String category;
    private int floor;

    public Room() {}

    public Room(Long id, String roomNumber, String category, int floor) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.category = category;
        this.floor = floor;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getFloor() { return floor; }
    public void setFloor(int floor) { this.floor = floor; }
}