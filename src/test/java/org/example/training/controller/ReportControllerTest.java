package org.example.training.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.training.dto.ParticipantsDto;
import org.example.training.dto.StudentDto;
import org.example.training.model.*;
import org.example.training.repository.CourseRepository;
import org.example.training.repository.StudentRepository;
import org.example.training.repository.StudyGroupRepository;
import org.example.training.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.training.utils.TestDataUtil.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReportControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  StudentRepository studentRepository;

  @Autowired
  TeacherRepository teacherRepository;

  @Autowired
  CourseRepository courseRepository;

  @Autowired
  StudyGroupRepository groupRepository;

  @Autowired
  ModelMapper modelMapper;

  @Test
  void countStudents_shouldReturnNumberOfStudents() throws Exception {
    studentRepository.save(modelMapper.map(studentDto(1), Student.class));
    studentRepository.save(modelMapper.map(studentDto(2), Student.class));

    mockMvc.perform(get("/api/report/students/count"))
      .andExpect(status().isOk())
      .andExpect(content().string("2"));
  }

  @Test
  void countTeachers_shouldReturnNumberOfTeachers() throws Exception {
    teacherRepository.save(modelMapper.map(teacherDto(1), Teacher.class));
    teacherRepository.save(modelMapper.map(teacherDto(2), Teacher.class));
    teacherRepository.save(modelMapper.map(teacherDto(3), Teacher.class));

    mockMvc.perform(get("/api/report/teachers/count"))
      .andExpect(status().isOk())
      .andExpect(content().string("3"));
  }

  @Test
  void countCoursesByType_shouldReturnGroupedCounts() throws Exception {
    courseRepository.save(modelMapper.map(courseMainDto("Math"), Course.class));
    courseRepository.save(modelMapper.map(courseMainDto("Physics"), Course.class));
    courseRepository.save(modelMapper.map(courseSecDto("History"), Course.class));

    MvcResult result = mockMvc.perform(
        get("/api/report/courses/count-by-type")
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andReturn();

    Map<CourseType, Integer> response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), new TypeReference<>() {});

    assertThat(response)
      .containsEntry(CourseType.MAIN, 2)
      .containsEntry(CourseType.SECONDARY, 1);
  }

  @Test
  void findStudentsByCourse_shouldReturnStudentsRegisteredForCourse() throws Exception {
    Course course = courseRepository.save(modelMapper.map(courseMainDto("Math"), Course.class));
    Student s1 = modelMapper.map(studentDto(1), Student.class);
    Student s2 = modelMapper.map(studentDto(2), Student.class);

    s1.getStudentCourses().add(course);
    s2.getStudentCourses().add(course);

    studentRepository.save(s1);
    studentRepository.save(s2);

    MvcResult result = mockMvc.perform(
        get("/api/report/students/course/{courseId}", course.getCourseId())
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andReturn();

    Page<StudentDto> page = readPage(result, StudentDto.class);
    assertThat(page.getContent()).hasSize(2);
  }

  @Test
  void findStudentsByGroup_shouldReturnStudentsFromGroup() throws Exception {
    StudyGroup group = groupRepository.save(modelMapper.map(groupDto(1), StudyGroup.class));

    Student s1 = modelMapper.map(studentDto(1), Student.class);
    Student s2 = modelMapper.map(studentDto(2), Student.class);

    s1.setStudentGroup(group);
    s2.setStudentGroup(group);

    studentRepository.save(s1);
    studentRepository.save(s2);

    MvcResult result = mockMvc.perform(
        get("/api/report/students/group/{groupId}", group.getGroupId())
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andReturn();

    Page<StudentDto> page = readPage(result, StudentDto.class);
    assertThat(page.getContent()).hasSize(2);
  }

  @Test
  void findStudentsByAgeAndCourse_shouldFilterCorrectly() throws Exception {
    Course course = courseRepository.save(modelMapper.map(courseMainDto("Math"), Course.class));

    Student younger = modelMapper.map(studentDto(1), Student.class);
    younger.setStudentAge(18);
    younger.getStudentCourses().add(course);

    Student older = modelMapper.map(studentDto(2), Student.class);
    older.setStudentAge(25);
    older.getStudentCourses().add(course);

    studentRepository.save(younger);
    studentRepository.save(older);

    MvcResult result = mockMvc.perform(
        get("/api/report/students/older/{minAge}/course/{courseId}", 21, course.getCourseId())
          .accept(MediaType.APPLICATION_JSON)
      )
      .andDo(print())
      .andExpect(status().isOk())
      .andReturn();

    Page<StudentDto> page = readPage(result, StudentDto.class);

    assertThat(page.getContent()).hasSize(1);

    assertThat(page.getContent().get(0).getStudentName())
      .isEqualTo(older.getStudentName());
  }

  @Test
  void findParticipants_shouldReturnStudentsAndTeachers() throws Exception {
    Course course = courseRepository.save(modelMapper.map(courseMainDto("Math"), Course.class));
    StudyGroup group = groupRepository.save(modelMapper.map(groupDto(1), StudyGroup.class));

    Student student = modelMapper.map(studentDto(1), Student.class);
    student.setStudentGroup(group);
    student.getStudentCourses().add(course);
    studentRepository.save(student);

    Teacher teacher = modelMapper.map(teacherDto(1), Teacher.class);
    teacher.getTeacherCourses().add(course);
    teacher.setTeacherGroup(group);
    teacherRepository.save(teacher);

    MvcResult result = mockMvc.perform(
        get("/api/report/participants/group/{groupId}/course/{courseId}",
          group.getGroupId(),
          course.getCourseId())
          .accept(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andReturn();

    ParticipantsDto dto =
      objectMapper.readValue(
        result.getResponse().getContentAsByteArray(),
        ParticipantsDto.class
      );

    assertThat(dto.getStudents()).hasSize(1);
    assertThat(dto.getTeachers()).hasSize(1);
  }

  private <T> Page<T> readPage(MvcResult result, Class<T> type) throws Exception {
    JsonNode root = objectMapper.readTree(result.getResponse().getContentAsByteArray());
    JsonNode contentNode = root.get("content");
    List<T> content = objectMapper.readValue(contentNode.toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, type));
    JsonNode totalNode = root.get("totalElements");
    long total = totalNode != null ? totalNode.asLong() : content.size();
    return new PageImpl<>(content, Pageable.unpaged(), total);
  }

}

