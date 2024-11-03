package com.karthik.calendar.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Configuration
public class CalendarConfig {

    @Value("${calendar.startOfDay}")
    private LocalTime startOfDayTime;

    @Value("${calendar.endOfDay}")
    private LocalTime endOfDayTime;

    @Value(("${calendar.meet.duration}"))
    private Integer durationInMins;

    public LocalDateTime getStartOfDay() {
        return LocalDateTime.of(LocalDate.now(), startOfDayTime);
    }

    public LocalDateTime getEndOfDay() {
        return LocalDateTime.of(LocalDate.now(), endOfDayTime);
    }

    public Integer getDurationInMins() {
        return durationInMins;
    }
}
