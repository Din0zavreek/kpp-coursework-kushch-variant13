package ua.edu.nupp.kpp.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExecutionRecord {
    private Long id;

    @JsonProperty("requestId")
    private Long requestId;

    @JsonProperty("reportDetails")
    private String resultNotes;

    @JsonProperty("completedDate")
    private String executionDate;

    public ExecutionRecord() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRequestId() { return requestId; }
    public void setRequestId(Long requestId) { this.requestId = requestId; }

    public String getResultNotes() { return resultNotes; }
    public void setResultNotes(String resultNotes) { this.resultNotes = resultNotes; }

    public String getExecutionDate() { return executionDate; }
    public void setExecutionDate(String executionDate) { this.executionDate = executionDate; }
}