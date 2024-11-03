package com.karthik.calendar.service.impl;

import com.karthik.calendar.model.*;
import com.karthik.calendar.service.helper.FreeSlotFinder;
import com.karthik.calendar.service.helper.MeetingConflictChecker;
import com.karthik.calendar.service.helper.MeetingScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CalendarServiceImplTest {

    @InjectMocks
    private CalendarServiceImpl calendarService;

    @Mock
    private MeetingScheduler meetingScheduler;

    @Mock
    private FreeSlotFinder freeSlotFinder;

    @Mock
    private MeetingConflictChecker meetingConflictChecker;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBookMeeting() {
        // Given
        Integer ownerId = 1;
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusHours(1);
        Set<Integer> participantIds = Set.of(2, 3);

        MeetingRequest meetingRequest = new MeetingRequest();
        meetingRequest.setOwnerId(ownerId);
        meetingRequest.setStartTime(startTime);
        meetingRequest.setEndTime(endTime);
        meetingRequest.setParticipantIds(participantIds);

        Meeting expectedMeeting = new Meeting();
        expectedMeeting.setOwnerId(ownerId);
        expectedMeeting.setStartTime(startTime);
        expectedMeeting.setEndTime(endTime);
        expectedMeeting.setParticipants(participantIds);

        // When
        when(meetingScheduler.scheduleMeeting(meetingRequest)).thenReturn(expectedMeeting);
        Meeting actualMeeting = calendarService.bookMeeting(meetingRequest);

        // Then
        assertEquals(expectedMeeting, actualMeeting);
        verify(meetingScheduler).scheduleMeeting(meetingRequest);
    }


    @Test
    public void testFindFreeSlots() {
        // Given
        Integer employee1Id = 1;
        Integer employee2Id = 2;
        List<TimeRange> expectedSlots = List.of(
                new TimeRange(LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3)),
                new TimeRange(LocalDateTime.now().plusHours(4), LocalDateTime.now().plusHours(5))
        );

        // When
        when(freeSlotFinder.findAvailableSlots(employee1Id, employee2Id)).thenReturn(expectedSlots);
        List<TimeRange> actualSlots = calendarService.findFreeSlots(employee1Id, employee2Id);

        // Then
        assertEquals(expectedSlots, actualSlots);
        verify(freeSlotFinder).findAvailableSlots(employee1Id, employee2Id);
    }

    @Test
    public void testFindConflictedParticipants() {
        // Given
        MeetingConflictRequest conflictRequest = new MeetingConflictRequest();
        conflictRequest.setStartTime(LocalDateTime.now().plusHours(1));
        conflictRequest.setEndTime(LocalDateTime.now().plusHours(2));
        conflictRequest.setParticipantIds(Set.of(2, 3));

        Set<Employee> expectedParticipants = Set.of(
                new Employee(2, "Alice"),
                new Employee(3, "Bob")
        );

        // When
        when(meetingConflictChecker.getConflictedParticipants(conflictRequest)).thenReturn(expectedParticipants);
        Set<Employee> actualParticipants = calendarService.findConflictedParticipants(conflictRequest);

        // Then
        assertEquals(expectedParticipants, actualParticipants);
        verify(meetingConflictChecker).getConflictedParticipants(conflictRequest);
    }

}
