package org.example.training.controller;

import jakarta.validation.Valid;
import org.example.training.dto.StudentDto;
import org.example.training.dto.filter.StudentFilter;
import org.example.training.model.Student;
import org.example.training.service.StudentQueryService;
import org.example.training.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Tag(name = "student", description = "CRUD student service")
@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController implements CrudController<StudentDto, StudentFilter, Student> {

  private final StudentService studentService;
  private final StudentQueryService studentQueryService;
  private final ModelMapper modelMapper;

  @Operation(operationId = "createStudent")
  @Override
  public StudentDto create(@Valid @RequestBody StudentDto studentDto) {
    return toDto(studentService.createStudent(toEntity(studentDto)));
  }

  @Operation(operationId = "updateStudent")
  @Override
  public StudentDto update(@PathVariable int id, @Valid @RequestBody StudentDto studentDto) {
    Student student = toEntity(studentDto);
    student.setStudentId(id);
    return toDto(studentService.updateStudent(student));
  }

  @Operation(operationId = "deleteStudent")
  @Override
  public void delete(@PathVariable int id) {
    studentService.deleteStudent(id);
  }

  @Operation(operationId = "findStudent")
  @Override
  public Page<StudentDto> find(@ParameterObject @ModelAttribute StudentFilter filter) {
    return studentQueryService.findStudent(filter).map(this::toDto);
  }

  @Operation(operationId = "getStudentById")
  @Override
  public StudentDto getById(@PathVariable int id) {
    return toDto(studentQueryService.getStudentById(id));
  }

  @Override
  public Student toEntity(StudentDto dto) {
    return modelMapper.map(dto, Student.class);
  }

  @Override
  public StudentDto toDto(Student entity) {
    return modelMapper.map(entity, StudentDto.class);
  }

}
