package com.karthik.calendar.service.helper;

import com.karthik.calendar.config.CalendarConfig;
import com.karthik.calendar.entity.Employee;
import com.karthik.calendar.model.TimeRange;
import com.karthik.calendar.repo.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
public class FreeSlotFinder {

    @Autowired
    private CalendarConfig config;

    @Autowired
    private EmployeeRepo employeeRepo;

    public List<TimeRange> findAvailableSlots(Integer employee1Id, Integer employee2Id) {
        Employee employee1 = findEmployeeById(employee1Id);
        Employee employee2 = findEmployeeById(employee2Id);

        List<TimeRange> allMeetings = getAllMeetings(employee1, employee2);
        LocalDateTime startOfDay = config.getStartOfDay();
        LocalDateTime endOfDay = config.getEndOfDay();
        int durationInMins = config.getDurationInMins();

        return getFreeSlots(allMeetings, startOfDay, endOfDay, durationInMins);
    }

    private List<TimeRange> getAllMeetings(Employee employee1, Employee employee2) {
        return Stream.concat(employee1.getMeetings().stream(), employee2.getMeetings().stream())
                .map(meeting -> new TimeRange(meeting.getStartTime(), meeting.getEndTime()))
                .toList();
    }

    private List<TimeRange> getFreeSlots(List<TimeRange> allMeetings, LocalDateTime startOfDay, LocalDateTime endOfDay, int durationInMins) {
        List<TimeRange> freeSlots = new ArrayList<>();
        LocalDateTime slotStart = startOfDay;

        while (slotStart.isBefore(endOfDay)) {
            LocalDateTime slotEnd = slotStart.plusMinutes(durationInMins);
            TimeRange slot = new TimeRange(slotStart, slotEnd);
            if (isSlotFree(allMeetings, slot)) {
                freeSlots.add(slot);
            }
            slotStart = slotEnd;
        }
        return freeSlots;
    }

    private boolean isSlotFree(List<TimeRange> allMeetings, TimeRange slot) {
        return allMeetings.stream().noneMatch(slot::overlaps);
    }

    private Employee findEmployeeById(Integer employeeId) {
        return employeeRepo.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + employeeId));
    }
}
