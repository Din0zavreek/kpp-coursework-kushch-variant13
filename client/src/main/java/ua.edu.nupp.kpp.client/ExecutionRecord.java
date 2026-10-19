package ua.edu.nupp.kpp.client;

public class ExecutionRecord {
    private Long id;
    private Long requestId;
    private String executionDate;
    private String resultNotes;

    public ExecutionRecord() {}

    public ExecutionRecord(Long id, Long requestId, String executionDate, String resultNotes) {
        this.id = id;
        this.requestId = requestId;
        this.executionDate = executionDate;
        this.resultNotes = resultNotes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }
    public String getExecutionDate() { return executionDate; }
    public void setExecutionDate(String executionDate) { this.executionDate = executionDate; }
    public String getResultNotes() { return resultNotes; }
    public void setResultNotes(String resultNotes) { this.resultNotes = resultNotes; }
}