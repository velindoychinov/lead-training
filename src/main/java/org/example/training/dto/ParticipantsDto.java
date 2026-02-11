package org.example.training.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipantsDto {
  private List<TeacherDto> teachers;
  private List<StudentDto> students;
}
