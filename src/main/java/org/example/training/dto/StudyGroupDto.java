package org.example.training.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO for {@link org.example.training.model.StudyGroup}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyGroupDto {

  private Integer groupId;

  @NotBlank(message = "name is required")
  @Size(min = 1, max = 50, message = "name must be between 1 and 50 characters")
  private String groupName;

}