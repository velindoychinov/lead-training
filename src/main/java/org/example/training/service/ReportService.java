package org.example.training.service;

import lombok.RequiredArgsConstructor;
import org.example.training.dto.ParticipantsDto;
import org.example.training.dto.StudentDto;
import org.example.training.dto.TeacherDto;
import org.example.training.dto.filter.ParticipantFilter;
import org.example.training.dto.filter.StudentFilter;
import org.example.training.dto.filter.TeacherFilter;
import org.example.training.model.*;
import org.example.training.repository.CourseRepository;
import org.example.training.repository.StudentRepository;
import org.example.training.repository.TeacherRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {

  private final StudentRepository studentRepository;
  private final TeacherRepository teacherRepository;
  private final CourseRepository courseRepository;
  private final StudentQueryService studentQueryService;
  private final TeacherQueryService teacherQueryService;
  private final ModelMapper modelMapper;

  public Long countStudents() {
    return studentRepository.count();
  }

  public Long countTeachers() {
    return teacherRepository.count();
  }

  public Map<CourseType, Integer> countCoursesByType() {
    return courseRepository.countByType()
      .stream()
      .collect(Collectors.toMap(
        r -> (CourseType) r[0],
        r -> ((Long) r[1]).intValue()
      ));
  }

  public Page<StudentDto> findStudentsByCourse(Integer courseId) {
    StudentFilter filter = StudentFilter.builder()
      .courseId(courseId)
      .build();

    return studentQueryService.findStudent(filter).map(this::toStudentDto);
  }

  public Page<StudentDto> findStudentsByGroup(Integer groupId) {
    StudentFilter filter = StudentFilter.builder()
      .groupId(groupId)
      .build();

    return studentQueryService.findStudent(filter).map(this::toStudentDto);
  }

  public Page<StudentDto> findStudentsByAgeAndCourse(int minAge, Integer courseId) {
    StudentFilter filter = StudentFilter.builder()
      .courseId(courseId)
      .fromAge(minAge)
      .build();

    return studentQueryService.findStudent(filter).map(this::toStudentDto);
  }

  public ParticipantsDto findParticipants(ParticipantFilter filter) {
    TeacherFilter teacherFilter = TeacherFilter.builder()
      .courseId(filter.getCourseId())
      .groupId(filter.getGroupId())
      .page(0)
      .size(countStudents().intValue())
      .teacherName(filter.getStudentName())
      .build();
    List<TeacherDto> teachers = teacherQueryService.findTeacher(teacherFilter)
      .stream().map(this::toTeacherDto).toList();

    StudentFilter studentFilter = StudentFilter.builder()
      .courseId(filter.getCourseId())
      .groupId(filter.getGroupId())
      .page(0)
      .size(countStudents().intValue())
      .studentName(filter.getStudentName())
      .build();
    List<StudentDto> students = studentQueryService.findStudent(studentFilter)
      .stream().map(this::toStudentDto).toList();

    return new ParticipantsDto(teachers, students);
  }

  private TeacherDto toTeacherDto(Teacher entity) {
    return modelMapper.map(entity, TeacherDto.class);
  }

  private StudentDto toStudentDto(Student entity) {
    return modelMapper.map(entity, StudentDto.class);
  }

}
