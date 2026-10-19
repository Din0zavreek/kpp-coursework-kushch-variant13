package ua.edu.nupp.kpp.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExecutionRecord {
    private Long id;

    @JsonProperty("request_id")
    private Long requestId;

    @JsonProperty("report_details")
    private String resultNotes;   // або reportDetails, але гетери/сетери мають збігатися

    @JsonProperty("completed_date")
    private String executionDate; // або completedDate

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }

    public String getResultNotes() { return resultNotes; }
    public void setResultNotes(String resultNotes) { this.resultNotes = resultNotes; }

    public String getExecutionDate() { return executionDate; }
    public void setExecutionDate(String executionDate) { this.executionDate = executionDate; }
}