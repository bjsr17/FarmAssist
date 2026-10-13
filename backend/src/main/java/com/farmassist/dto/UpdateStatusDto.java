package com.farmassist.dto;

import com.farmassist.entity.RequestStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateStatusDto {

    @NotNull(message = "Status is required")
    private RequestStatus status;

    private String comment;
    private Double finalCost;
    private String executionNotes;

    public UpdateStatusDto() {
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(Double finalCost) {
        this.finalCost = finalCost;
    }

    public String getExecutionNotes() {
        return executionNotes;
    }

    public void setExecutionNotes(String executionNotes) {
        this.executionNotes = executionNotes;
    }
}
