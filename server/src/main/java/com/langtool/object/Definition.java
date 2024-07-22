package com.langtool.object;

import java.time.LocalDateTime;

public class Definition {
    private String definition;
    private LocalDateTime submitted;

    // Getters and Setters
    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public LocalDateTime getSubmitted() {
        return submitted;
    }

    public void setSubmitted(LocalDateTime submitted) {
        this.submitted = submitted;
    }
}