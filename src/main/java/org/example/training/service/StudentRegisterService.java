package org.example.training.service;

import lombok.RequiredArgsConstructor;
import org.example.training.model.Course;
import org.example.training.model.StudyGroup;
import org.example.training.model.Student;
import org.example.training.repository.CourseRepository;
import org.example.training.repository.StudyGroupRepository;
import org.example.training.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentRegisterService {

  private final StudentRepository studentRepository;
  private final CourseRepository courseRepository;
  private final StudyGroupRepository groupRepository;
  
  public void addStudentToCourse(Integer studentId, Integer courseId) {
    Student student = studentRepository
      .findById(studentId)
      .orElseThrow(() -> new IllegalStateException("Student not found"));
    
    Course course = courseRepository.getReferenceById(courseId);
    student.getStudentCourses().add(course);
    studentRepository.save(student);
  }
  
  public void removeStudentFromCourse(Integer studentId, Integer courseId) {
    Student student = studentRepository
      .findById(studentId)
      .orElseThrow(() -> new IllegalStateException("Student not found"));
    
    Course course = courseRepository.getReferenceById(courseId);
    student.getStudentCourses().remove(course);
    studentRepository.save(student);
  }

  public void addStudentToGroup(Integer studentId, Integer groupId) {
    Student student = studentRepository
      .findById(studentId)
      .orElseThrow(() -> new IllegalStateException("Student not found"));

    StudyGroup group = groupRepository.getReferenceById(groupId);
    student.setStudentGroup(group);
    studentRepository.save(student);
  }

}
