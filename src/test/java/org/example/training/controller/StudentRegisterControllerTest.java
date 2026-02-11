package org.example.training.controller;

import org.example.training.model.Course;
import org.example.training.model.Student;
import org.example.training.model.StudyGroup;
import org.example.training.repository.CourseRepository;
import org.example.training.repository.StudentRepository;
import org.example.training.repository.StudyGroupRepository;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.training.utils.TestDataUtil.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class StudentRegisterControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private StudyGroupRepository studyGroupRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Test
  void addStudentToCourse_shouldRegisterStudentInCourse() throws Exception {
    Student student = studentRepository.save(modelMapper.map(studentDto(1), Student.class));
    Course course = courseRepository.save(modelMapper.map(courseMainDto("Math"), Course.class));

    mockMvc.perform(post("/api/register/student/{studentId}/course/{courseId}",
        student.getStudentId(),
        course.getCourseId()))
      .andExpect(status().isCreated());

    Student updated = studentRepository.findById(student.getStudentId()).orElseThrow();
    assertThat(updated.getStudentCourses())
      .extracting(Course::getCourseId)
      .contains(course.getCourseId());
  }

  @Test
  void removeStudentFromCourse_shouldUnregisterStudentFromCourse() throws Exception {
    Student student = studentRepository.save(modelMapper.map(studentDto(1), Student.class));
    Course course = courseRepository.save(modelMapper.map(courseMainDto("Math"), Course.class));

    // prepare relation
    student.getStudentCourses().add(course);
    studentRepository.save(student);

    mockMvc.perform(delete("/api/register/student/{studentId}/course/{courseId}",
        student.getStudentId(),
        course.getCourseId()))
      .andExpect(status().isOk());

    Student updated = studentRepository.findById(student.getStudentId()).orElseThrow();

    assertThat(updated.getStudentCourses())
      .extracting(Course::getCourseId)
      .doesNotContain(course.getCourseId());
  }

  @Test
  void addStudentToGroup_shouldAssignStudentToGroup() throws Exception {
    Student student = studentRepository.save(modelMapper.map(studentDto(1), Student.class));
    StudyGroup group = studyGroupRepository.save(modelMapper.map(groupDto(1), StudyGroup.class));

    mockMvc.perform(post("/api/register/student/{studentId}/group/{groupId}",
        student.getStudentId(),
        group.getGroupId()))
      .andExpect(status().isCreated());

    Student updated = studentRepository.findById(student.getStudentId()).orElseThrow();

    assertThat(updated.getStudentGroup()).isNotNull();
    assertThat(updated.getStudentGroup().getGroupId())
      .isEqualTo(group.getGroupId());
  }

}

