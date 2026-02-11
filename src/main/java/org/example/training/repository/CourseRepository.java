package org.example.training.repository;

import org.example.training.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer>, JpaSpecificationExecutor<Course> {

  @Query("""
    SELECT c.courseType, COUNT(c)
    FROM Course c
    GROUP BY c.courseType
    """)
  List<Object[]> countByType();

}