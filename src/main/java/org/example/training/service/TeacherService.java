package org.example.training.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.training.model.Teacher;
import org.example.training.repository.TeacherRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@RequiredArgsConstructor
@Validated
public class TeacherService {
  
  private final TeacherRepository teacherRepository;

  public Teacher createTeacher(@Valid Teacher teacher) {
    teacher.setTeacherId(null);
    return teacherRepository.save(teacher);
  }

  public Teacher updateTeacher(@Valid Teacher changes) {
    Teacher entity = teacherRepository
      .findById(changes.getTeacherId())
      .orElseThrow(() -> new IllegalStateException("Teacher not found"));

    BeanUtils.copyProperties(changes, entity, Teacher.Fields.teacherId);

    return teacherRepository.save(entity);
  }

  public void deleteTeacher(Integer id) {
    Teacher teacher = teacherRepository.findById(id)
      .orElseThrow(() -> new IllegalStateException("Teacher not found"));
    teacherRepository.delete(teacher);
  }

}
