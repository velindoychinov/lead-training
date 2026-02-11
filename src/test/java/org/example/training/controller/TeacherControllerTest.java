package org.example.training.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.training.dto.TeacherDto;
import org.example.training.model.Teacher;
import org.example.training.repository.TeacherRepository;
import org.example.training.utils.PageResponse;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.training.utils.TestDataUtil.assertEqualsExcept;
import static org.example.training.utils.TestDataUtil.teacherDto;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TeacherControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private TeacherRepository teacherRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Test
  void createTeacher_shouldReturnCreatedTeacher() throws Exception {

    TeacherDto request = teacherDto(1);

    MvcResult result = mockMvc.perform(post("/api/teacher")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isCreated())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andReturn();

    TeacherDto response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), TeacherDto.class);

    TeacherDto expected = request;
    assertEqualsExcept(response, expected,"teacherId");
    assertThat(response.getTeacherId()).isNotNull();

    Teacher saved = teacherRepository.findById(response.getTeacherId()).orElseThrow();
    TeacherDto savedDto = modelMapper.map(saved, TeacherDto.class);
    assertEqualsExcept(savedDto, expected,"teacherId");
  }

  @Test
  void updateTeacher_shouldModifyExistingTeacher() throws Exception {
    Teacher saved = teacherRepository.save(modelMapper.map(teacherDto(1), Teacher.class));

    TeacherDto update = teacherDto(2);

    MvcResult result = mockMvc.perform(put("/api/teacher/{id}", saved.getTeacherId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(update)))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andReturn();

    TeacherDto response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), TeacherDto.class);
    assertThat(response.getTeacherId()).isEqualTo(saved.getTeacherId());
    assertEqualsExcept(response, update,"teacherId");

    Teacher updated = teacherRepository.findById(saved.getTeacherId()).orElseThrow();
    TeacherDto updatedDto = modelMapper.map(updated, TeacherDto.class);
    assertEqualsExcept(updatedDto, update,"teacherId");
  }

  @Test
  void deleteTeacher_shouldRemoveTeacher() throws Exception {
    Teacher saved = teacherRepository.save(modelMapper.map(teacherDto(1), Teacher.class));

    mockMvc.perform(delete("/api/teacher/{id}", saved.getTeacherId()))
      .andExpect(status().isOk());

    assertThat(teacherRepository.existsById(saved.getTeacherId())).isFalse();
  }

  @Test
  void getTeacherById_shouldReturnTeacher() throws Exception {
    Teacher saved = teacherRepository.save(modelMapper.map(teacherDto(1), Teacher.class));

    MvcResult result = mockMvc.perform(get("/api/teacher/{id}", saved.getTeacherId())
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andReturn();

    TeacherDto response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), TeacherDto.class);
    TeacherDto expected = modelMapper.map(saved, TeacherDto.class);
    assertEqualsExcept(response, expected,"teacherId");
    assertThat(response.getTeacherId()).isEqualTo(saved.getTeacherId());
  }

  @Test
  void findTeachers_shouldReturnAllTeachers() throws Exception {
    teacherRepository.save(modelMapper.map(teacherDto(1), Teacher.class));
    teacherRepository.save(modelMapper.map(teacherDto(2), Teacher.class));

    MvcResult result = mockMvc.perform(get("/api/teacher/find")
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andReturn();

    JavaType pageType = objectMapper.getTypeFactory().constructParametricType(PageResponse.class, TeacherDto.class);
    PageResponse<TeacherDto> page = objectMapper.readValue(result.getResponse().getContentAsByteArray(), pageType);
    assertThat(page.getContent()).hasSize(2);
  }
}


