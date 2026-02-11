package org.example.training.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.training.dto.CourseDto;
import org.example.training.dto.filter.CourseFilter;
import org.example.training.model.Course;
import org.example.training.service.CourseService;
import org.modelmapper.ModelMapper;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Tag(name = "course", description = "CRUD course service")
@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController implements CrudController<CourseDto, CourseFilter, Course> {

  private final CourseService courseService;
  private final ModelMapper modelMapper;

  @Operation(operationId = "createCourse")
  @Override
  public CourseDto create(@Valid @RequestBody CourseDto courseDto) {
    return toDto(courseService.createCourse(toEntity(courseDto)));
  }

  @Operation(operationId = "updateCourse")
  @Override
  public CourseDto update(@PathVariable int id, @Valid @RequestBody CourseDto courseDto) {
    Course course = toEntity(courseDto);
    course.setCourseId(id);
    return toDto(courseService.updateCourse(course));
  }

  @Operation(operationId = "deleteCourse")
  @Override
  public void delete(@PathVariable int id) {
    courseService.deleteCourse(id);
  }

  @Operation(operationId = "findCourse")
  @Override
  public Page<CourseDto> find(@ParameterObject @ModelAttribute CourseFilter filter) {
    return courseService.findCourse(filter).map(this::toDto);
  }

  @Operation(operationId = "getCourseById")
  @Override
  public CourseDto getById(@PathVariable int id) {
    return toDto(courseService.getCourseById(id));
  }

  @Override
  public Course toEntity(CourseDto dto) {
    return modelMapper.map(dto, Course.class);
  }

  @Override
  public CourseDto toDto(Course entity) {
    return modelMapper.map(entity, CourseDto.class);
  }

}
