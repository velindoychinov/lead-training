package org.example.training.service;

import lombok.RequiredArgsConstructor;
import org.example.training.dto.filter.StudentFilter;
import org.example.training.model.Course;
import org.example.training.model.Student;
import org.example.training.model.StudyGroup;
import org.example.training.repository.StudentRepository;
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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentQueryService {

  private final StudentRepository studentRepository;

  public Student getStudentById(Integer id) {
    return studentRepository.findById(id).orElseThrow(() -> new IllegalStateException("Student not found"));
  }

  final List<Sort.Order> defaultSort = List.of(
    Sort.Order.asc(Student.Fields.studentName),
    Sort.Order.desc(Student.Fields.studentId)
  );

  public Page<Student> findStudent(StudentFilter filter) {
    PageRequest paging = ServiceUtils.paging(filter, defaultSort);

    Specification<Student> spec = (root, query, cb) -> cb.conjunction();

    spec = spec.and(anyLike(filter.getStudentName(), r -> r.get(Student.Fields.studentName)));
    spec = spec.and(in(listOf(filter.getGroupId()), r -> r.join(Student.Fields.studentGroup).get(StudyGroup.Fields.groupId)));
    spec = spec.and(in(listOf(filter.getCourseId()), r -> r.join(Student.Fields.studentCourses).get(Course.Fields.courseId)));
    spec = spec.and(between(filter.getFromAge(), filter.getToAge(), r -> r.get(Student.Fields.studentAge)));

    return studentRepository.findAll(spec, paging);
  }
  
}
