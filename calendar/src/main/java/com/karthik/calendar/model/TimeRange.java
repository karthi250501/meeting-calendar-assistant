package com.karthik.calendar.model;

import java.time.LocalDateTime;

public class TimeRange {
    LocalDateTime start;
    LocalDateTime end;

    public TimeRange(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public boolean overlaps(TimeRange other) {
        return (this.start.isBefore(other.end) && other.start.isBefore(this.end));
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
