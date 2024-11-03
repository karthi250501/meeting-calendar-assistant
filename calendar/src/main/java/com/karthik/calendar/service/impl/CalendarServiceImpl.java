package com.karthik.calendar.service.impl;

import com.karthik.calendar.model.*;
import com.karthik.calendar.service.CalendarService;
import com.karthik.calendar.service.helper.FreeSlotFinder;
import com.karthik.calendar.service.helper.MeetingConflictChecker;
import com.karthik.calendar.service.helper.MeetingScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CalendarServiceImpl implements CalendarService {

    @Autowired
    private MeetingScheduler meetingScheduler;

    @Autowired
    private FreeSlotFinder freeSlotFinder;

    @Autowired
    private MeetingConflictChecker meetingConflictChecker;

    @Override
    public Meeting bookMeeting(MeetingRequest meetingRequest) {
        return meetingScheduler.scheduleMeeting(meetingRequest);
    }

    @Override
    public List<TimeRange> findFreeSlots(Integer employee1Id, Integer employee2Id) {
        return freeSlotFinder.findAvailableSlots(employee1Id, employee2Id);
    }

    @Override
    public Set<Employee> findConflictedParticipants(MeetingConflictRequest meetingConflictRequest) {
        return meetingConflictChecker.getConflictedParticipants(meetingConflictRequest);
    }
}
