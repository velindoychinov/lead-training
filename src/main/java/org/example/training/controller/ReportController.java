package org.example.training.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.training.dto.ParticipantsDto;
import org.example.training.dto.StudentDto;
import org.example.training.dto.filter.ParticipantFilter;
import org.example.training.model.CourseType;
import org.example.training.service.ReportService;
import org.example.training.service.StudentQueryService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "report", description = "report service")
@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

  private final ReportService reportService;

  @Operation(operationId = "countStudents")
  @GetMapping(
    value = "/students/count"
  )
  @ResponseStatus(HttpStatus.OK)
  public Long countStudents() {
    return reportService.countStudents();
  }

  @Operation(operationId = "countTeachers")
  @GetMapping(
    value = "/teachers/count"
  )
  @ResponseStatus(HttpStatus.OK)
  public Long countTeachers() {
    return reportService.countTeachers();
  }

  @Operation(operationId = "countCoursesByType")
  @GetMapping(
    value = "/courses/count-by-type",
    produces = "application/json"
  )
  @ResponseStatus(HttpStatus.OK)
  public Map<CourseType, Integer> countCoursesByType() {
    return reportService.countCoursesByType();
  }

  @Operation(operationId = "findStudentsByCourse")
  @GetMapping(
    value = "/students/course/{courseId}",
    produces = "application/json"
  )
  @ResponseStatus(HttpStatus.OK)
  public Page<StudentDto> findStudentsByCourse(@PathVariable Integer courseId) {
    return reportService.findStudentsByCourse(courseId);
  }

  @Operation(operationId = "findStudentsByGroup")
  @GetMapping(
    value = "/students/group/{groupId}",
    produces = "application/json"
  )
  @ResponseStatus(HttpStatus.OK)
  public Page<StudentDto> findStudentsByGroup(@PathVariable Integer groupId) {
    return reportService.findStudentsByGroup(groupId);
  }

  @Operation(operationId = "findStudentsByGroup")
  @GetMapping(
    value = "/students/older/{minAge}/course/{courseId}",
    produces = "application/json"
  )
  @ResponseStatus(HttpStatus.OK)
  public Page<StudentDto> findStudentsByAgeAndCourse(@PathVariable int minAge, @PathVariable Integer courseId) {
    return reportService.findStudentsByAgeAndCourse(minAge, courseId);
  }

  @Operation(operationId = "participants")
  @GetMapping(
    value = "/participants/group/{groupId}/course/{courseId}",
    produces = "application/json"
  )
  @ResponseStatus(HttpStatus.OK)
  public ParticipantsDto findParticipants(@PathVariable Integer groupId, @PathVariable Integer courseId) {
    ParticipantFilter filter = ParticipantFilter.builder()
      .groupId(groupId)
      .courseId(courseId)
      .build();
    return reportService.findParticipants(filter);
  }

}
