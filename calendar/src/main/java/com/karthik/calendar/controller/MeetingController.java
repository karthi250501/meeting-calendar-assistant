package com.karthik.calendar.controller;

import com.karthik.calendar.model.*;
import com.karthik.calendar.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/meet")
public class MeetingController {

    @Autowired
    private CalendarService calendarService;

    @PostMapping("/book")
    public ResponseEntity<Meeting> bookMeeting(@RequestBody MeetingRequest meetingRequest) {
        return ResponseEntity.ok(calendarService.bookMeeting(meetingRequest));
    }

    @GetMapping("/free-slots")
    public ResponseEntity<List<TimeRange>> findFreeSlots(@RequestParam Integer user1Id, @RequestParam Integer user2Id) {
        List<TimeRange> freeSlots = calendarService.findFreeSlots(user1Id, user2Id);
        return ResponseEntity.ok(freeSlots);
    }

    @PostMapping("/check-conflicts")
    public ResponseEntity<Set<Employee>> checkConflicts(@RequestBody MeetingConflictRequest meetingConflictRequest) {
        Set<Employee> conflictedParticipants = calendarService.findConflictedParticipants(meetingConflictRequest);
        return ResponseEntity.ok(conflictedParticipants);
    }

}
