package com.api.marketbridge.commun;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Standard error response")
public class ApiError {
    @Schema(description = "Timestamp when the error occurred")
    private LocalDateTime timestamp = LocalDateTime.now();

    @Schema(description = "Error message")
    private String message;

    @Schema(description = "Detailed error description")
    private String details;

    // Getters and setters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}