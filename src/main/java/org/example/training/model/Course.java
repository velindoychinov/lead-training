package org.example.training.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.example.training.utils.CourseTypeConverter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Builder
@Getter
@Setter
@Entity
@Table(name = "course")
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "course_id")
  private Integer courseId;

  @Column(name = "course_name")
  private String courseName;

  @Convert(converter = CourseTypeConverter.class)
  @Column(name = "course_type")
  private CourseType courseType;

  @ManyToMany(mappedBy = "studentCourses")
  private Set<Student> students;

  @ManyToMany(mappedBy = "teacherCourses")
  private Set<Teacher> teachers;

}