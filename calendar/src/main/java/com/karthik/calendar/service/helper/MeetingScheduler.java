package com.karthik.calendar.service.helper;

import com.karthik.calendar.entity.Employee;
import com.karthik.calendar.entity.Meeting;
import com.karthik.calendar.model.MeetingRequest;
import com.karthik.calendar.repo.EmployeeRepo;
import com.karthik.calendar.repo.MeetingRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MeetingScheduler {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private MeetingRepo meetingRepo;

    public com.karthik.calendar.model.Meeting scheduleMeeting(MeetingRequest meetingRequest) {
        Integer ownerId = meetingRequest.getOwnerId();
        LocalDateTime startTime = meetingRequest.getStartTime();
        LocalDateTime endTime = meetingRequest.getEndTime();
        Set<Integer> participantIds = meetingRequest.getParticipantIds();

        validateTimings(startTime, endTime);
        validateParticipants(ownerId, participantIds);
        checkOwnerConflict(ownerId, startTime, endTime);

        Employee owner = fetchEmployeeById(ownerId, "Owner not found : " + ownerId);
        Set<Employee> participants = fetchParticipantsByIds(participantIds);

        Meeting meeting = createMeeting(owner, startTime, endTime, participants);
        return Converter.toMeeting(meetingRepo.save(meeting));
    }

    private void validateTimings(LocalDateTime startTime, LocalDateTime endTime) {
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("End Time is Before Start Time | Cannot Book Meet");
        }
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot Book Meet in Past");
        }
    }

    private void validateParticipants(Integer ownerId, Set<Integer> participantIds) {
        if (participantIds.isEmpty()) {
            throw new IllegalArgumentException("No participants given");
        }
        if (participantIds.contains(ownerId)) {
            throw new IllegalArgumentException("Participants contain Owner ID: " + ownerId);
        }
    }

    private void checkOwnerConflict(Integer ownerId, LocalDateTime startTime, LocalDateTime endTime) {
        boolean hasConflict = !meetingRepo.findByOwnerIdAndTimeRange(ownerId, startTime, endTime).isEmpty();
        if (hasConflict) {
            throw new IllegalArgumentException("Owner has a conflicting meeting");
        }
    }

    private Employee fetchEmployeeById(Integer employeeId, String errorMessage) {
        return employeeRepo.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(errorMessage));
    }

    private Set<Employee> fetchParticipantsByIds(Set<Integer> participantIds) {
        return participantIds.stream()
                .map(id -> fetchEmployeeById(id, "Participant not found : " + id))
                .collect(Collectors.toSet());
    }

    private Meeting createMeeting(Employee owner, LocalDateTime startTime, LocalDateTime endTime, Set<Employee> participants) {
        Meeting meeting = new Meeting();
        meeting.setStartTime(startTime);
        meeting.setEndTime(endTime);
        meeting.setOwner(owner);
        meeting.setParticipants(participants);
        participants.forEach(participant -> participant.getParticipatingMeetings().add(meeting));
        return meeting;
    }
}
