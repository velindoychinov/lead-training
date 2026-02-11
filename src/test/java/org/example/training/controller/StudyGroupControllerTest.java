package org.example.training.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.training.model.StudyGroup;
import org.example.training.dto.StudyGroupDto;
import org.example.training.repository.StudyGroupRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.training.utils.TestDataUtil.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class StudyGroupControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private StudyGroupRepository studyGroupRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Test
  void createStudyGroup_shouldReturnCreatedGroup() throws Exception {

    StudyGroupDto request = groupDto(1);

    MvcResult result = mockMvc.perform(post("/api/group")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isCreated())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andReturn();

    StudyGroupDto response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), StudyGroupDto.class);

    StudyGroupDto expected = request;
    assertEqualsExcept(response, expected,"groupId");
    assertThat(response.getGroupId()).isNotNull();

    StudyGroup saved = studyGroupRepository.findById(response.getGroupId()).orElseThrow();
    StudyGroupDto savedDto = modelMapper.map(saved, StudyGroupDto.class);
    assertEqualsExcept(savedDto, expected,"groupId");
  }

  @Test
  void updateStudyGroup_shouldModifyExistingGroup() throws Exception {
    StudyGroup saved = studyGroupRepository.save(modelMapper.map(groupDto(1), StudyGroup.class));

    StudyGroupDto update = groupDto(2);

    MvcResult result = mockMvc.perform(put("/api/group/{id}", saved.getGroupId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(update)))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andReturn();

    StudyGroupDto response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), StudyGroupDto.class);
    assertThat(response.getGroupId()).isEqualTo(saved.getGroupId());
    assertEqualsExcept(response, update,"groupId");

    StudyGroup updated = studyGroupRepository.findById(saved.getGroupId()).orElseThrow();
    StudyGroupDto updatedDto = modelMapper.map(updated, StudyGroupDto.class);
    assertEqualsExcept(updatedDto, update,"groupId");
  }

  @Test
  void deleteStudyGroup_shouldRemoveGroup() throws Exception {
    StudyGroup saved = studyGroupRepository.save(modelMapper.map(groupDto(1), StudyGroup.class));

    mockMvc.perform(delete("/api/group/{id}", saved.getGroupId()))
      .andExpect(status().isOk());

    assertThat(studyGroupRepository.existsById(saved.getGroupId())).isFalse();
  }

  @Test
  void getStudyGroupById_shouldReturnGroup() throws Exception {
    StudyGroup saved = studyGroupRepository.save(modelMapper.map(groupDto(1), StudyGroup.class));

    MvcResult result = mockMvc.perform(get("/api/group/{id}", saved.getGroupId())
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andReturn();

    StudyGroupDto response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), StudyGroupDto.class);
    StudyGroupDto expected = modelMapper.map(saved, StudyGroupDto.class);
    assertEqualsExcept(response, expected,"groupId");
    assertThat(response.getGroupId()).isEqualTo(saved.getGroupId());
  }

  @Test
  void findStudyGroups_shouldReturnAllGroups() throws Exception {
    studyGroupRepository.save(modelMapper.map(groupDto(1), StudyGroup.class));
    studyGroupRepository.save(modelMapper.map(groupDto(2), StudyGroup.class));

    MvcResult result = mockMvc.perform(get("/api/group/find")
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andReturn();

    JavaType pageType = objectMapper.getTypeFactory().constructParametricType(PageResponse.class, StudyGroupDto.class);
    PageResponse<StudyGroupDto> page = objectMapper.readValue(result.getResponse().getContentAsByteArray(), pageType);
    assertThat(page.getContent()).hasSize(2);
  }
}


