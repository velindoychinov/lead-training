package org.example.training.dto;

import lombok.*;
import org.example.training.model.CourseType;

import java.io.Serializable;

/**
 * DTO for {@link org.example.training.model.Course}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDto {
  private Integer courseId;
  private String courseName;
  private CourseType courseType;
}