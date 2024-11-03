package com.karthik.calendar.service;

import com.karthik.calendar.model.Employee;
import com.karthik.calendar.model.Meeting;
import com.karthik.calendar.model.MeetingConflictRequest;
import com.karthik.calendar.model.MeetingRequest;
import com.karthik.calendar.model.TimeRange;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface CalendarService {

    Meeting bookMeeting(MeetingRequest meetingRequest);

    List<TimeRange> findFreeSlots(Integer employee1Id, Integer employee2Id);

    Set<Employee> findConflictedParticipants(MeetingConflictRequest meetingConflictRequest);
}
