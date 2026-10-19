package ua.edu.nupp.kpp.server.Models;

public class ExecutionRecord {
    private Long id;
    private Long requestId;
    private String reportDetails;
    private String completedDate;

    public ExecutionRecord() {}

    public ExecutionRecord(Long id, Long requestId, String reportDetails, String completedDate) {
        this.id = id;
        this.requestId = requestId;
        this.reportDetails = reportDetails;
        this.completedDate = completedDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }

    public String getReportDetails() { return reportDetails; }
    public void setReportDetails(String reportDetails) { this.reportDetails = reportDetails; }

    public String getCompletedDate() { return completedDate; }
    public void setCompletedDate(String completedDate) { this.completedDate = completedDate; }
}
