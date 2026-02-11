package org.example.training.service;

import lombok.RequiredArgsConstructor;
import org.example.training.model.Course;
import org.example.training.model.StudyGroup;
import org.example.training.model.Teacher;
import org.example.training.repository.CourseRepository;
import org.example.training.repository.StudyGroupRepository;
import org.example.training.repository.TeacherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TeacherRegisterService {

  private final TeacherRepository teacherRepository;
  private final CourseRepository courseRepository;
  private final StudyGroupRepository groupRepository;

  public void addTeacherToCourse(Integer teacherId, Integer courseId) {
    Teacher teacher = teacherRepository
      .findById(teacherId)
      .orElseThrow(() -> new IllegalStateException("Teacher not found"));

    Course course = courseRepository.getReferenceById(courseId);
    teacher.getTeacherCourses().add(course);
    teacherRepository.save(teacher);
  }

  public void removeTeacherFromCourse(Integer teacherId, Integer courseId) {
    Teacher teacher = teacherRepository
      .findById(teacherId)
      .orElseThrow(() -> new IllegalStateException("Teacher not found"));

    Course course = courseRepository.getReferenceById(courseId);
    teacher.getTeacherCourses().remove(course);
    teacherRepository.save(teacher);
  }

  public void addTeacherToGroup(Integer teacherId, Integer groupId) {
    Teacher teacher = teacherRepository
      .findById(teacherId)
      .orElseThrow(() -> new IllegalStateException("Teacher not found"));

    StudyGroup group = groupRepository.getReferenceById(groupId);
    teacher.setTeacherGroup(group);
    teacherRepository.save(teacher);
  }

}
