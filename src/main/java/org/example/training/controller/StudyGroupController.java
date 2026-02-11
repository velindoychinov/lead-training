package org.example.training.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.training.dto.StudyGroupDto;
import org.example.training.dto.filter.StudyGroupFilter;
import org.example.training.model.StudyGroup;
import org.example.training.service.StudyGroupService;
import org.modelmapper.ModelMapper;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Tag(name = "group", description = "CRUD group service")
@RestController
@RequestMapping("/api/group")
@RequiredArgsConstructor
public class StudyGroupController implements CrudController<StudyGroupDto, StudyGroupFilter, StudyGroup> {

  private final StudyGroupService studyGroupService;
  private final ModelMapper modelMapper;

  @Operation(operationId = "createStudyGroup")
  @Override
  public StudyGroupDto create(@Valid @RequestBody StudyGroupDto studyGroupDto) {
    return toDto(studyGroupService.createStudyGroup(toEntity(studyGroupDto)));
  }

  @Operation(operationId = "updateStudyGroup")
  @Override
  public StudyGroupDto update(@PathVariable int id, @Valid @RequestBody StudyGroupDto studyGroupDto) {
    StudyGroup studyGroup = toEntity(studyGroupDto);
    studyGroup.setGroupId(id);
    return toDto(studyGroupService.updateStudyGroup(studyGroup));
  }

  @Operation(operationId = "deleteStudyGroup")
  @Override
  public void delete(@PathVariable int id) {
    studyGroupService.deleteStudyGroup(id);
  }

  @Operation(operationId = "findStudyGroup")
  @Override
  public Page<StudyGroupDto> find(@ParameterObject @ModelAttribute StudyGroupFilter filter) {
    return studyGroupService.findStudyGroup(filter).map(this::toDto);
  }

  @Operation(operationId = "getStudyGroupById")
  @Override
  public StudyGroupDto getById(@PathVariable int id) {
    return toDto(studyGroupService.getStudyGroupById(id));
  }

  @Override
  public StudyGroup toEntity(StudyGroupDto dto) {
    return modelMapper.map(dto, StudyGroup.class);
  }

  @Override
  public StudyGroupDto toDto(StudyGroup entity) {
    return modelMapper.map(entity, StudyGroupDto.class);
  }

}
