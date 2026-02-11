package org.example.training.model;

import lombok.Getter;

@Getter
public enum CourseType {
  MAIN(0),
  SECONDARY(1);

  private final int code;
  CourseType(int code) { this.code = code; }

  public static CourseType fromCode(int code) {
    for (CourseType c : values()) {
      if (c.code == code) return c;
    }
    throw new IllegalArgumentException("Unknown code: " + code);
  }
}