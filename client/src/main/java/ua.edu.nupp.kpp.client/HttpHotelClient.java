package ua.edu.nupp.kpp.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class HttpHotelClient {
    private static final String BASE_URL = "http://localhost:8080/server";
    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Room> getRooms() {
        return Arrays.asList(read(get("/rooms"), Room[].class));
    }

    public void addRoom(Room room) {
        String form = (room.getId() != null ? "id=" + room.getId() + "&" : "") +
                "roomNumber=" + enc(room.getRoomNumber()) +
                "&category=" + enc(room.getCategory()) +
                "&floor=" + room.getFloor();
        post("/rooms", form);
    }

    public void deleteRoom(Long id) {
        post("/rooms", "action=delete&id=" + id);
    }

    public List<Specialist> getSpecialists() {
        return Arrays.asList(read(get("/specialists"), Specialist[].class));
    }

    public void addSpecialist(Specialist specialist) {
        String form = (specialist.getId() != null ? "id=" + specialist.getId() + "&" : "") +
                "fullName=" + enc(specialist.getFullName()) +
                "&specialization=" + enc(specialist.getSpecialization()) +
                "&phone=" + enc(specialist.getPhone());
        post("/specialists", form);
    }

    public void deleteSpecialist(Long id) {
        post("/specialists", "action=delete&id=" + id);
    }

    public List<Request> getRequests() {
        return Arrays.asList(read(get("/requests"), Request[].class));
    }

    public void addRequest(Request request) {
        String form = (request.getId() != null ? "id=" + request.getId() + "&" : "") +
                "roomId=" + request.getRoomId() +
                (request.getSpecialistId() != null ? "&specialistId=" + request.getSpecialistId() : "") +
                "&description=" + enc(request.getDescription()) +
                "&state=" + enc(request.getState()) +
                "&f1=" + request.getF1() +
                "&f2=" + request.getF2() +
                "&f3=" + request.getF3();
        post("/requests", form);
    }

    public void deleteRequest(Long id) {
        post("/requests", "action=delete&id=" + id);
    }

    public List<ExecutionRecord> getExecutionRecords() {
        return Arrays.asList(read(get("/execution-records"), ExecutionRecord[].class));
    }

    public void addExecutionRecord(ExecutionRecord record) {
        String form = (record.getId() != null ? "id=" + record.getId() + "&" : "") +
                "requestId=" + record.getRequestId() +
                "&completedDate=" + enc(record.getExecutionDate()) +
                "&reportDetails=" + enc(record.getResultNotes());
        post("/execution-records", form);
    }

    public void deleteExecutionRecord(Long id) {
        post("/execution-records", "action=delete&id=" + id);
    }

    private String get(String path) {
        HttpRequest req = HttpRequest.newBuilder(URI.create(BASE_URL + path)).GET().build();
        return send(req);
    }

    private String post(String path, String form) {
        HttpRequest req = HttpRequest.newBuilder(URI.create(BASE_URL + path))
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();
        return send(req);
    }

    private String send(HttpRequest req) {
        try {
            HttpResponse<String> r = http.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (r.statusCode() >= 400) {
                throw new RuntimeException(extractErrorMessage(r.body()));
            }
            return r.body();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Помилка з'єднання із сервером: " + e.getMessage(), e);
        }
    }

    private String extractErrorMessage(String body) {
        try {
            JsonNode node = mapper.readTree(body);
            if (node.has("error")) {
                return node.get("error").asText();
            }
        } catch (Exception ignored) {
        }
        return body;
    }

    private <T> T read(String json, Class<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException("Помилка розбору JSON: " + e.getMessage(), e);
        }
    }

    private String enc(String s) {
        return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8);
    }
}