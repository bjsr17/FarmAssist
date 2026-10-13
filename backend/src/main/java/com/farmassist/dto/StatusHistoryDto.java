package com.farmassist.dto;

import com.farmassist.entity.RequestStatus;
import java.time.LocalDateTime;

public class StatusHistoryDto {
    private Long id;
    private Long requestId;
    private RequestStatus status;
    private String comment;
    private String updatedByName;
    private LocalDateTime createdAt;

    public StatusHistoryDto() {
    }

    public StatusHistoryDto(Long id, Long requestId, RequestStatus status, String comment, String updatedByName, LocalDateTime createdAt) {
        this.id = id;
        this.requestId = requestId;
        this.status = status;
        this.comment = comment;
        this.updatedByName = updatedByName;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
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

    public String getUpdatedByName() {
        return updatedByName;
    }

    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
