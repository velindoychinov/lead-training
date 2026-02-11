package org.example.training.dto;

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
  private String groupName;
}