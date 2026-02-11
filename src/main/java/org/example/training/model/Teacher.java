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
@Table(name = "teacher")
public class Teacher {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "teacher_id")
  private Integer teacherId;

  @Column(name = "teacher_name")
  private String teacherName;

  @Column(name = "teacher_age")
  private int teacherAge;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "group_id")
  private StudyGroup teacherGroup;

  @ManyToMany
  @JoinTable(
    name = "teacher_course",
    joinColumns = @JoinColumn(name = "teacher_id"),
    inverseJoinColumns = @JoinColumn(name = "course_id")
  )
  private Set<Course> teacherCourses = new HashSet<>();

}