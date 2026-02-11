package org.example.training.service;

import lombok.RequiredArgsConstructor;
import org.example.training.dto.filter.TeacherFilter;
import org.example.training.model.Course;
import org.example.training.model.StudyGroup;
import org.example.training.model.Teacher;
import org.example.training.repository.TeacherRepository;
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
public class TeacherQueryService {
  
  private final TeacherRepository teacherRepository;

  public Teacher getTeacherById(Integer id) {
    return teacherRepository.findById(id).orElseThrow(() -> new IllegalStateException("Teacher not found"));
  }

  final List<Sort.Order> defaultSort = List.of(
    Sort.Order.asc(Teacher.Fields.teacherName),
    Sort.Order.desc(Teacher.Fields.teacherId)
  );

  public Page<Teacher> findTeacher(TeacherFilter filter) {
    PageRequest paging = ServiceUtils.paging(filter, defaultSort);

    Specification<Teacher> spec = (root, query, cb) -> cb.conjunction();

    spec = spec.and(anyLike(filter.getTeacherName(), r -> r.get(Teacher.Fields.teacherName)));
    spec = spec.and(in(listOf(filter.getGroupId()), r -> r.join(Teacher.Fields.teacherGroup).get(StudyGroup.Fields.groupId)));
    spec = spec.and(in(listOf(filter.getCourseId()), r -> r.join(Teacher.Fields.teacherCourses).get(Course.Fields.courseId)));

    return teacherRepository.findAll(spec, paging);
  }

}
