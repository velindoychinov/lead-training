package org.example.training.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.training.service.StudentRegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "student register", description = "Student register service")
@RestController
@RequestMapping("/api/register/student")
@RequiredArgsConstructor
public class StudentRegisterController {

  private final StudentRegisterService studentRegisterService;

  // student -> course

  @Operation(operationId = "addStudentToCourse")
  @PostMapping(
    value = "/{studentId}/course/{courseId}"
  )
  @ResponseStatus(HttpStatus.CREATED)
  void addStudentToCourse(@PathVariable Integer studentId, @PathVariable Integer courseId) {
    studentRegisterService.addStudentToCourse(studentId, courseId);
  }

  @Operation(operationId = "removeStudentFromCourse")
  @DeleteMapping(
    value = "/{studentId}/course/{courseId}"
  )
  @ResponseStatus(HttpStatus.OK)
  void removeStudentFromCourse(@PathVariable Integer studentId, @PathVariable Integer courseId) {
    studentRegisterService.removeStudentFromCourse(studentId, courseId);
  }

  // student -> group

  @Operation(operationId = "addStudentToGroup")
  @PostMapping(
    value = "/{studentId}/group/{groupId}"
  )
  @ResponseStatus(HttpStatus.CREATED)
  void addStudentToGroup(@PathVariable Integer studentId, @PathVariable Integer groupId) {
    studentRegisterService.addStudentToGroup(studentId, groupId);
  }

}
