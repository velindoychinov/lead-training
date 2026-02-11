package org.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.training.model.Course;
import org.example.training.model.StudyGroup;

import java.util.Set;

/**
 * DTO for {@link org.example.training.model.Student}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDto {
  Integer studentId;
  String studentName;
  int studentAge;
}