package org.example.training.utils;

import org.example.training.dto.*;
import org.example.training.model.CourseType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestDataUtil {

  public static <T> void assertEqualsExcept(
    T actual,
    T expected,
    String... ignoredFields
  ) {

    assertThat(actual)
      .usingRecursiveComparison()
      .ignoringFields(ignoredFields)
      .isEqualTo(expected);
  }

  public static CourseDto courseMainDto(String name) {
    return CourseDto.builder()
      .courseName(name)
      .courseType(CourseType.MAIN)
      .build();
  }

  public static CourseDto courseSecDto(String name) {
    return CourseDto.builder()
      .courseName(name)
      .courseType(CourseType.SECONDARY)
      .build();
  }

  public static StudentDto studentDto(int num) {
    return StudentDto.builder()
      .studentName("Student #" + num)
      .studentAge(20)
      .build();
  }

  public static TeacherDto teacherDto(int num) {
    return TeacherDto.builder()
      .teacherName("Teacher #" + num)
      .build();
  }

  public static StudyGroupDto groupDto(int num) {
    return StudyGroupDto.builder()
      .groupName("Group #" + num)
      .build();
  }

  public static ParticipantsDto participantsDto(List<TeacherDto> teachers, List<StudentDto> students) {
    ParticipantsDto dto = new ParticipantsDto();
    dto.setTeachers(teachers);
    dto.setStudents(students);
    return dto;
  }

}
