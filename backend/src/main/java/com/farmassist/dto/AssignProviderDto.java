package com.farmassist.dto;

import jakarta.validation.constraints.NotNull;

public class AssignProviderDto {

    @NotNull(message = "Provider ID is required")
    private Long providerId;

    private String assignmentNotes;

    public AssignProviderDto() {
    }

    public AssignProviderDto(Long providerId, String assignmentNotes) {
        this.providerId = providerId;
        this.assignmentNotes = assignmentNotes;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public String getAssignmentNotes() {
        return assignmentNotes;
    }

    public void setAssignmentNotes(String assignmentNotes) {
        this.assignmentNotes = assignmentNotes;
    }
}
