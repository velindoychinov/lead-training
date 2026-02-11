package org.example.training.service;

import lombok.RequiredArgsConstructor;
import org.example.training.dto.filter.CourseFilter;
import org.example.training.model.Course;
import org.example.training.repository.CourseRepository;
import org.example.training.utils.ServiceUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.example.training.utils.ServiceUtils.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {

  private final CourseRepository courseRepository;

  public Course createCourse(Course course) {
    course.setCourseId(null);
    return courseRepository.save(course);
  }

  public Course updateCourse(Course changes) {
    Course entity = courseRepository
      .findById(changes.getCourseId())
      .orElseThrow(() -> new IllegalStateException("Course not found"));

    entity.setCourseType(changes.getCourseType());
    entity.setCourseName(changes.getCourseName());

    return courseRepository.save(entity);
  }

  public void deleteCourse(Integer id) {
    Course course = courseRepository.findById(id)
      .orElseThrow(() -> new IllegalStateException("Course not found"));
    courseRepository.delete(course);
  }

  @Transactional(readOnly = true)
  public Course getCourseById(Integer id) {
    return courseRepository.findById(id).orElseThrow(() -> new IllegalStateException("Course not found"));
  }

  final List<Sort.Order> defaultSort = List.of(
    Sort.Order.asc(Course.Fields.courseName),
    Sort.Order.desc(Course.Fields.courseId)
  );

  @Transactional(readOnly = true)
  public Page<Course> findCourse(CourseFilter filter) {
    PageRequest paging = ServiceUtils.paging(filter, defaultSort);

    Specification<Course> spec = (root, query, cb) -> cb.conjunction();

    spec = spec.and(anyLike(filter.getCourseName(), r -> r.get(Course.Fields.courseName)));
    spec = spec.and(eq(filter.getCourseType(), r -> r.get(Course.Fields.courseType)));

    return courseRepository.findAll(spec, paging);
  }
  
}
