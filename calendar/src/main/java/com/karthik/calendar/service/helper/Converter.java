package com.karthik.calendar.service.helper;

import com.karthik.calendar.model.Employee;
import com.karthik.calendar.model.Meeting;

import java.util.stream.Collectors;

public class Converter {

    public static Employee toEmployee(com.karthik.calendar.entity.Employee employee) {
        return new Employee(employee.getEmployeeId(), employee.getEmployeeName());
    }

    public static Meeting toMeeting(com.karthik.calendar.entity.Meeting meeting) {
        Meeting meetingVO = new Meeting();
        meetingVO.setOwnerId(meeting.getOwner().getEmployeeId());
        meetingVO.setStartTime(meeting.getStartTime());
        meetingVO.setEndTime(meeting.getEndTime());
        meetingVO.setParticipants(meeting.getParticipants().stream().map(com.karthik.calendar.entity.Employee::getEmployeeId).collect(Collectors.toSet()));
        return meetingVO;
    }
}
