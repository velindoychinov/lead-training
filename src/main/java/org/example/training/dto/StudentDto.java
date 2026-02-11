package org.example.training.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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