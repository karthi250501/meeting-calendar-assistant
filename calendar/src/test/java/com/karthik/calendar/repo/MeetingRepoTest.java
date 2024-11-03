package com.karthik.calendar.repo;

import com.karthik.calendar.entity.Employee;
import com.karthik.calendar.entity.Meeting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MeetingRepoTest {

    @Autowired
    private MeetingRepo meetingRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    private Employee owner;

    @BeforeEach
    void setUp() {
        meetingRepo.deleteAll();
        employeeRepo.deleteAll();

        owner = new Employee();
        owner.setEmployeeName("Meeting Owner");
        owner = employeeRepo.save(owner);
    }

    @Test
    void testSaveAndFindMeeting() {
        Meeting meeting = new Meeting();
        meeting.setOwner(owner);
        meeting.setStartTime(LocalDateTime.now());
        meeting.setEndTime(LocalDateTime.now().plusHours(1));

        meetingRepo.save(meeting);
        assertThat(meetingRepo.findById(meeting.getMeetingId())).isPresent();
    }

    @Test
    void testFindByOwnerIdAndTimeRange() {
        Meeting meeting = new Meeting();
        meeting.setOwner(owner);
        meeting.setStartTime(LocalDateTime.now());
        meeting.setEndTime(LocalDateTime.now().plusHours(1));
        meetingRepo.save(meeting);

        LocalDateTime startTime = LocalDateTime.now().minusMinutes(30);
        LocalDateTime endTime = LocalDateTime.now().plusMinutes(30);
        var meetings = meetingRepo.findByOwnerIdAndTimeRange(owner.getEmployeeId(), startTime, endTime);

        assertThat(meetings).isNotEmpty();
    }

    @Test
    void testFindConflictingMeetings() {
        Meeting meeting = new Meeting();
        meeting.setOwner(owner);
        meeting.setStartTime(LocalDateTime.now());
        meeting.setEndTime(LocalDateTime.now().plusHours(1));
        meetingRepo.save(meeting);

        Meeting conflictingMeeting = new Meeting();
        conflictingMeeting.setOwner(owner);
        conflictingMeeting.setStartTime(LocalDateTime.now().plusMinutes(30));
        conflictingMeeting.setEndTime(LocalDateTime.now().plusHours(2));
        meetingRepo.save(conflictingMeeting);

        var conflicts = meetingRepo.findConflictingMeetings(owner.getEmployeeId(), LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        assertThat(conflicts).hasSize(2);
    }
}
