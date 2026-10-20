package ua.edu.nupp.kpp.client;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HttpHotelClient {
    private static final String BASE_URL = "http://localhost:8080/server";
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Room> getRooms() {
        List<Room> rooms = new ArrayList<>();
        try {
            URL url = new URL(BASE_URL + "/rooms");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                Room[] array = mapper.readValue(reader, Room[].class);
                rooms = Arrays.asList(array);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public void addRoom(Room room) {
        try {
            URL url = new URL(BASE_URL + "/rooms");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            String params = (room.getId() != null ? "id=" + room.getId() + "&" : "") +
                    "roomNumber=" + java.net.URLEncoder.encode(room.getRoomNumber(), StandardCharsets.UTF_8) +
                    "&category=" + java.net.URLEncoder.encode(room.getCategory(), StandardCharsets.UTF_8) +
                    "&floor=" + room.getFloor();

            try (OutputStream os = conn.getOutputStream()) {
                os.write(params.getBytes(StandardCharsets.UTF_8));
            }
            conn.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteRoom(Long id) {
        try {
            URL url = new URL(BASE_URL + "/rooms?action=delete&id=" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Specialist> getSpecialists() {
        List<Specialist> specialists = new ArrayList<>();
        try {
            URL url = new URL(BASE_URL + "/specialists");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                Specialist[] array = mapper.readValue(reader, Specialist[].class);
                specialists = Arrays.asList(array);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return specialists;
    }

    public void addSpecialist(Specialist specialist) {
        try {
            URL url = new URL(BASE_URL + "/specialists");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            String params = (specialist.getId() != null ? "id=" + specialist.getId() + "&" : "") +
                    "fullName=" + java.net.URLEncoder.encode(specialist.getFullName(), StandardCharsets.UTF_8) +
                    "&specialization=" + java.net.URLEncoder.encode(specialist.getSpecialization(), StandardCharsets.UTF_8) +
                    "&phone=" + java.net.URLEncoder.encode(specialist.getPhone(), StandardCharsets.UTF_8);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(params.getBytes(StandardCharsets.UTF_8));
            }
            conn.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteSpecialist(Long id) {
        try {
            URL url = new URL(BASE_URL + "/specialists?action=delete&id=" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Request> getRequests() {
        List<Request> requests = new ArrayList<>();
        try {
            URL url = new URL(BASE_URL + "/requests");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                Request[] array = mapper.readValue(reader, Request[].class);
                requests = Arrays.asList(array);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requests;
    }

    public void addRequest(Request request) {
        try {
            URL url = new URL(BASE_URL + "/requests");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            String params = (request.getId() != null ? "id=" + request.getId() + "&" : "") +
                    "roomId=" + request.getRoomId() +
                    (request.getSpecialistId() != null ? "&specialistId=" + request.getSpecialistId() : "") +
                    "&description=" + java.net.URLEncoder.encode(request.getDescription(), StandardCharsets.UTF_8) +
                    "&state=" + java.net.URLEncoder.encode(request.getState(), StandardCharsets.UTF_8) +
                    "&f1=" + request.getF1() +
                    "&f2=" + request.getF2() +
                    "&f3=" + request.getF3();

            try (OutputStream os = conn.getOutputStream()) {
                os.write(params.getBytes(StandardCharsets.UTF_8));
            }
            conn.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteRequest(Long id) {
        try {
            URL url = new URL(BASE_URL + "/requests?action=delete&id=" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ExecutionRecord> getExecutionRecords() {
        List<ExecutionRecord> records = new ArrayList<>();
        try {
            URL url = new URL(BASE_URL + "/execution-records");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    ExecutionRecord[] array = mapper.readValue(reader, ExecutionRecord[].class);
                    if (array != null) {
                        records = Arrays.asList(array);
                    }
                }
            } else {
                System.out.println("Сервер повернув код помилки для execution-records: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return records;
    }

    public void addExecutionRecord(ExecutionRecord record) {
        try {
            URL url = new URL(BASE_URL + "/execution-records");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            String params = (record.getId() != null ? "id=" + record.getId() + "&" : "") +
                    "requestId=" + record.getRequestId() +
                    "&completedDate=" + java.net.URLEncoder.encode(record.getExecutionDate(), StandardCharsets.UTF_8) +
                    "&reportDetails=" + java.net.URLEncoder.encode(record.getResultNotes(), StandardCharsets.UTF_8);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(params.getBytes(StandardCharsets.UTF_8));
            }
            conn.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteExecutionRecord(Long id) {
        try {
            URL url = new URL(BASE_URL + "/execution-records?action=delete&id=" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}