package org.example.training.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipantFilter {

  private String teacherName;
  private String studentName;
  private Integer groupId;
  private Integer courseId;
  private Integer fromAge;
  private Integer toAge;

}
