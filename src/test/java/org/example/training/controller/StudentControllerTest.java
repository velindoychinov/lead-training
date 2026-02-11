package org.example.training.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.training.dto.StudentDto;
import org.example.training.model.Student;
import org.example.training.repository.StudentRepository;
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
import static org.example.training.utils.TestDataUtil.studentDto;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Test
  void createStudent_shouldReturnCreatedStudent() throws Exception {

    StudentDto request = studentDto(1);

    MvcResult result = mockMvc.perform(post("/api/student")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isCreated())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andReturn();

    StudentDto response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), StudentDto.class);

    StudentDto expected = request;
    assertEqualsExcept(response, expected,"studentId");
    assertThat(response.getStudentId()).isNotNull();

    Student saved = studentRepository.findById(response.getStudentId()).orElseThrow();
    StudentDto savedDto = modelMapper.map(saved, StudentDto.class);
    assertEqualsExcept(savedDto, expected,"studentId");
  }

  @Test
  void updateStudent_shouldModifyExistingStudent() throws Exception {
    Student saved = studentRepository.save(modelMapper.map(studentDto(1), Student.class));

    StudentDto update = studentDto(2);

    MvcResult result = mockMvc.perform(put("/api/student/{id}", saved.getStudentId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(update)))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andReturn();

    StudentDto response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), StudentDto.class);
    assertThat(response.getStudentId()).isEqualTo(saved.getStudentId());
    assertEqualsExcept(response, update,"studentId");

    Student updated = studentRepository.findById(saved.getStudentId()).orElseThrow();
    StudentDto updatedDto = modelMapper.map(updated, StudentDto.class);
    assertEqualsExcept(updatedDto, update,"studentId");
  }

  @Test
  void deleteStudent_shouldRemoveStudent() throws Exception {
    Student saved = studentRepository.save(modelMapper.map(studentDto(1), Student.class));

    mockMvc.perform(delete("/api/student/{id}", saved.getStudentId()))
      .andExpect(status().isOk());

    assertThat(studentRepository.existsById(saved.getStudentId())).isFalse();
  }

  @Test
  void getStudentById_shouldReturnStudent() throws Exception {
    Student saved = studentRepository.save(modelMapper.map(studentDto(1), Student.class));

    MvcResult result = mockMvc.perform(get("/api/student/{id}", saved.getStudentId())
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andReturn();

    StudentDto response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), StudentDto.class);
    StudentDto expected = modelMapper.map(saved, StudentDto.class);
    assertEqualsExcept(response, expected,"studentId");
    assertThat(response.getStudentId()).isEqualTo(saved.getStudentId());
  }

  @Test
  void findStudents_shouldReturnAllStudents() throws Exception {
    studentRepository.save(modelMapper.map(studentDto(1), Student.class));
    studentRepository.save(modelMapper.map(studentDto(2), Student.class));

    MvcResult result = mockMvc.perform(get("/api/student/find")
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andReturn();

    JavaType pageType = objectMapper.getTypeFactory().constructParametricType(PageResponse.class, StudentDto.class);
    PageResponse<StudentDto> page = objectMapper.readValue(result.getResponse().getContentAsByteArray(), pageType);
    assertThat(page.getContent()).hasSize(2);
  }
}


