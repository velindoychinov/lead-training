package org.example.training.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.training.dto.CourseDto;
import org.example.training.model.Course;
import org.example.training.repository.CourseRepository;
import org.example.training.utils.PageResponse;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.training.utils.TestDataUtil.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CourseControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Test
  void createCourse_shouldReturnCreatedCourse() throws Exception {
    CourseDto requestDto = courseMainDto("Math");

    MvcResult result = mockMvc.perform(post("/api/course")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(requestDto)))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andReturn();

    CourseDto response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), CourseDto.class);
    CourseDto expected = courseMainDto("Math");
    assertEqualsExcept(response, expected,"courseId");

    List<Course> courses = courseRepository.findAll();
    assertThat(courses).hasSize(1);
    
    Course expectedEntity = modelMapper.map(expected, Course.class);
    assertEqualsExcept(courses.get(0), expectedEntity,"courseId");
  }

  @Test
  void updateCourse_shouldModifyExistingCourse() throws Exception {
    Course saved = courseRepository.save(modelMapper.map(courseMainDto("Math"), Course.class));
    CourseDto updateDto = courseSecDto("Physics");

    MvcResult result = mockMvc.perform(put("/api/course/{id}", saved.getCourseId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateDto)))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andReturn();

    CourseDto response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), CourseDto.class);
    CourseDto expectedDto = courseSecDto("Physics");
    assertEqualsExcept(response, expectedDto,"courseId");

    Course updated = courseRepository.findById(saved.getCourseId()).orElseThrow();
    Course expectedEntity = modelMapper.map(expectedDto, Course.class);
    assertEqualsExcept(updated, expectedEntity,"courseId");
  }

  @Test
  void deleteCourse_shouldRemoveCourse() throws Exception {
    Course saved = courseRepository.save(modelMapper.map(courseMainDto("Math"), Course.class));

    mockMvc.perform(delete("/api/course/{id}", saved.getCourseId()))
      .andExpect(status().isOk());

    assertThat(courseRepository.existsById(saved.getCourseId())).isFalse();
  }

  @Test
  void getCourseById_shouldReturnCourse() throws Exception {
    Course saved = courseRepository.save(modelMapper.map(courseMainDto("Math"), Course.class));

    MvcResult result = mockMvc.perform(get("/api/course/{id}", saved.getCourseId())
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andReturn();

    CourseDto response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), CourseDto.class);
    CourseDto expected = courseMainDto("Math");
    assertEqualsExcept(response, expected,"courseId");
  }

  @Test
  void findCourses_shouldReturnAllCourses() throws Exception {
    courseRepository.save(modelMapper.map(courseMainDto("Math"), Course.class));
    courseRepository.save(modelMapper.map(courseSecDto("Physics"), Course.class));

    MvcResult result = mockMvc.perform(get("/api/course/find")
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andReturn();

    JavaType pageType = objectMapper.getTypeFactory().constructParametricType(PageResponse.class, CourseDto.class);
    PageResponse<CourseDto> page = objectMapper.readValue(result.getResponse().getContentAsByteArray(), pageType);
    assertThat(page.getContent()).hasSize(2);

    List<String> names = page.getContent().stream()
      .map(CourseDto::getCourseName)
      .toList();

    assertThat(names).containsExactlyInAnyOrder("Math", "Physics");
  }
  
}
