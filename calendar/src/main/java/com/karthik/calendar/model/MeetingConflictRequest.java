package com.karthik.calendar.model;

import java.time.LocalDateTime;
import java.util.Set;

public class MeetingConflictRequest {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Set<Integer> participantIds;

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Set<Integer> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(Set<Integer> participantIds) {
        this.participantIds = participantIds;
    }
}
