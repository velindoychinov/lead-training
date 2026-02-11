package org.example.training.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.training.dto.TeacherDto;
import org.example.training.dto.filter.TeacherFilter;
import org.example.training.model.Teacher;
import org.example.training.service.TeacherQueryService;
import org.example.training.service.TeacherService;
import org.modelmapper.ModelMapper;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Tag(name = "teacher", description = "CRUD teacher service")
@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController implements CrudController<TeacherDto, TeacherFilter, Teacher> {

  private final TeacherService teacherService;
  private final TeacherQueryService teacherQueryService;
  private final ModelMapper modelMapper;

  @Operation(operationId = "createTeacher")
  @Override
  public TeacherDto create(@RequestBody TeacherDto teacherDto) {
    return toDto(teacherService.createTeacher(toEntity(teacherDto)));
  }

  @Operation(operationId = "updateTeacher")
  @Override
  public TeacherDto update(@PathVariable int id, @RequestBody TeacherDto teacherDto) {
    Teacher teacher = toEntity(teacherDto);
    teacher.setTeacherId(id);
    return toDto(teacherService.updateTeacher(teacher));
  }

  @Operation(operationId = "deleteTeacher")
  @Override
  public void delete(@PathVariable int id) {
    teacherService.deleteTeacher(id);
  }

  @Operation(operationId = "findTeacher")
  @Override
  public Page<TeacherDto> find(@ParameterObject @ModelAttribute TeacherFilter filter) {
    return teacherQueryService.findTeacher(filter).map(this::toDto);
  }

  @Operation(operationId = "getTeacherById")
  @Override
  public TeacherDto getById(@PathVariable int id) {
    return toDto(teacherQueryService.getTeacherById(id));
  }

  @Override
  public Teacher toEntity(TeacherDto dto) {
    return modelMapper.map(dto, Teacher.class);
  }

  @Override
  public TeacherDto toDto(Teacher entity) {
    return modelMapper.map(entity, TeacherDto.class);
  }

}
