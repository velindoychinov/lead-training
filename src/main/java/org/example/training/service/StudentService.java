package org.example.training.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.training.model.Student;
import org.example.training.repository.StudentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@RequiredArgsConstructor
@Validated
public class StudentService {

  private final StudentRepository studentRepository;

  public Student createStudent(@Valid Student student) {
    student.setStudentId(null);
    return studentRepository.save(student);
  }

  public Student updateStudent(@Valid Student changes) {
    Student entity = studentRepository
      .findById(changes.getStudentId())
      .orElseThrow(() -> new IllegalStateException("Student not found"));

    BeanUtils.copyProperties(changes, entity, Student.Fields.studentId);

    return studentRepository.save(entity);
  }

  public void deleteStudent(Integer id) {
    Student student = studentRepository.findById(id)
      .orElseThrow(() -> new IllegalStateException("Student not found"));
    studentRepository.delete(student);
  }

}
