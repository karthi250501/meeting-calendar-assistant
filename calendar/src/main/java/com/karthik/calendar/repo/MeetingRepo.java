package com.karthik.calendar.repo;

import com.karthik.calendar.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MeetingRepo extends JpaRepository<Meeting, Integer> {

    @Query("SELECT m FROM Meeting m WHERE m.owner.employeeId = :ownerId AND ((m.startTime < :endTime) AND (m.endTime > :startTime))")
    List<Meeting> findByOwnerIdAndTimeRange(@Param("ownerId") Integer ownerId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT m FROM Meeting m WHERE m.owner.employeeId = :employeeId AND ((m.startTime <= :endTime AND m.endTime >= :startTime))")
    List<Meeting> findConflictingMeetings(@Param("employeeId") Integer employeeId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
