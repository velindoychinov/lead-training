package org.example.training.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

  @NotBlank(message = "name is required")
  @Size(min = 2, max = 100, message = "name must be between 2 and 50 characters")
  String studentName;

  @NotNull(message = "age is required")
  int studentAge;

}