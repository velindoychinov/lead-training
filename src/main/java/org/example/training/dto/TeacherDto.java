package org.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link org.example.training.model.Teacher}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherDto {
  Integer teacherId;
  String teacherName;
}