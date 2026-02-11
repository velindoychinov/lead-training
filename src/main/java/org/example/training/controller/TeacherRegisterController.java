package org.example.training.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.training.service.TeacherRegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "teacher register", description = "Teacher register service")
@RestController
@RequestMapping("/api/register/teacher")
@RequiredArgsConstructor
public class TeacherRegisterController {

  private final TeacherRegisterService teacherRegisterService;

  // teacher -> course

  @Operation(operationId = "addTeacherToCourse")
  @PostMapping(
    value = "/{teacherId}/course/{courseId}"
  )
  @ResponseStatus(HttpStatus.CREATED)
  void addTeacherToCourse(@PathVariable Integer teacherId, @PathVariable Integer courseId) {
    teacherRegisterService.addTeacherToCourse(teacherId, courseId);
  }

  @Operation(operationId = "removeTeacherFromCourse")
  @DeleteMapping(
    value = "/{teacherId}/course/{courseId}"
  )
  @ResponseStatus(HttpStatus.OK)
  void removeTeacherFromCourse(@PathVariable Integer teacherId, @PathVariable Integer courseId) {
    teacherRegisterService.removeTeacherFromCourse(teacherId, courseId);
  }

  // teacher -> group

  @Operation(operationId = "addTeacherToGroup")
  @PostMapping(
    value = "/{teacherId}/group/{groupId}"
  )
  @ResponseStatus(HttpStatus.CREATED)
  void addTeacherToGroup(@PathVariable Integer teacherId, @PathVariable Integer groupId) {
    teacherRegisterService.addTeacherToGroup(teacherId, groupId);
  }

}
