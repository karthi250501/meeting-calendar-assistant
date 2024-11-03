package com.karthik.calendar.service.helper;

import com.karthik.calendar.model.Employee;
import com.karthik.calendar.model.MeetingConflictRequest;
import com.karthik.calendar.repo.EmployeeRepo;
import com.karthik.calendar.repo.MeetingRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MeetingConflictChecker {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private MeetingRepo meetingRepo;

    public Set<Employee> getConflictedParticipants(MeetingConflictRequest meetingConflictRequest) {
        Set<Integer> participantIds = meetingConflictRequest.getParticipantIds();
        LocalDateTime startTime = meetingConflictRequest.getStartTime();
        LocalDateTime endTime = meetingConflictRequest.getEndTime();

        return participantIds.stream()
                .map(id -> fetchConflictingParticipant(id, startTime, endTime))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    private Optional<Employee> fetchConflictingParticipant(Integer id, LocalDateTime startTime, LocalDateTime endTime) {
        com.karthik.calendar.entity.Employee employee = employeeRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Participant not found : " + id));
        boolean hasConflict = !meetingRepo.findConflictingMeetings(id, startTime, endTime).isEmpty();
        return hasConflict ? Optional.of(Converter.toEmployee(employee)) : Optional.empty();
    }
}
