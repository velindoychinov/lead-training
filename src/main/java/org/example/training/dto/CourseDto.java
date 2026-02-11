package org.example.training.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.training.model.CourseType;

/**
 * DTO for {@link org.example.training.model.Course}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDto {

  private Integer courseId;

  @NotBlank(message = "name is required")
  @Size(min = 2, max = 100, message = "name must be between 2 and 100 characters")
  private String courseName;

  @NotNull(message = "type is required")
  private CourseType courseType;

}