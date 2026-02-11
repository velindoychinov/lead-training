package org.example.training.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Builder
@Getter
@Setter
@Entity
@Table(name = "study_group")
public class StudyGroup {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "group_id")
  private Integer groupId;

  @Column(name = "group_name")
  private String groupName;

  @OneToMany(mappedBy = "studentGroup")
  private Set<Student> students = new HashSet<>();

  @OneToMany(mappedBy = "teacherGroup")
  private Set<Teacher> teachers = new HashSet<>();

}