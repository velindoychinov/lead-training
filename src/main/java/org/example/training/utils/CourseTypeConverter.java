package org.example.training.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.training.model.CourseType;

@Converter(autoApply = true)
public class CourseTypeConverter implements AttributeConverter<CourseType, Integer> {
  @Override
  public Integer convertToDatabaseColumn(CourseType attribute) {
    return attribute != null ? attribute.getCode() : null;
  }

  @Override
  public CourseType convertToEntityAttribute(Integer dbData) {
    return dbData != null ? CourseType.fromCode(dbData) : null;
  }
}

