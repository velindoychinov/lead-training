package org.example.training.controller;

import org.example.training.model.Course;
import org.example.training.model.Teacher;
import org.example.training.model.StudyGroup;
import org.example.training.repository.CourseRepository;
import org.example.training.repository.TeacherRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TeacherRegisterControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TeacherRepository teacherRepository;

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private StudyGroupRepository studyGroupRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Test
  void addTeacherToCourse_shouldRegisterTeacherInCourse() throws Exception {
    Teacher teacher = teacherRepository.save(modelMapper.map(teacherDto(1), Teacher.class));
    Course course = courseRepository.save(modelMapper.map(courseMainDto("Math"), Course.class));

    mockMvc.perform(post("/api/register/teacher/{teacherId}/course/{courseId}",
        teacher.getTeacherId(),
        course.getCourseId()))
      .andExpect(status().isCreated());

    Teacher updated = teacherRepository.findById(teacher.getTeacherId()).orElseThrow();
    assertThat(updated.getTeacherCourses())
      .extracting(Course::getCourseId)
      .contains(course.getCourseId());
  }

  @Test
  void removeTeacherFromCourse_shouldUnregisterTeacherFromCourse() throws Exception {
    Teacher teacher = teacherRepository.save(modelMapper.map(teacherDto(1), Teacher.class));
    Course course = courseRepository.save(modelMapper.map(courseMainDto("Math"), Course.class));

    // prepare relation
    teacher.getTeacherCourses().add(course);
    teacherRepository.save(teacher);

    mockMvc.perform(delete("/api/register/teacher/{teacherId}/course/{courseId}",
        teacher.getTeacherId(),
        course.getCourseId()))
      .andExpect(status().isOk());

    Teacher updated = teacherRepository.findById(teacher.getTeacherId()).orElseThrow();

    assertThat(updated.getTeacherCourses())
      .extracting(Course::getCourseId)
      .doesNotContain(course.getCourseId());
  }

  @Test
  void addTeacherToGroup_shouldAssignTeacherToGroup() throws Exception {
    Teacher teacher = teacherRepository.save(modelMapper.map(teacherDto(1), Teacher.class));
    StudyGroup group = studyGroupRepository.save(modelMapper.map(groupDto(1), StudyGroup.class));

    mockMvc.perform(post("/api/register/teacher/{teacherId}/group/{groupId}",
        teacher.getTeacherId(),
        group.getGroupId()))
      .andExpect(status().isCreated());

    Teacher updated = teacherRepository.findById(teacher.getTeacherId()).orElseThrow();

    assertThat(updated.getTeacherGroup()).isNotNull();
    assertThat(updated.getTeacherGroup().getGroupId())
      .isEqualTo(group.getGroupId());
  }
  
}

