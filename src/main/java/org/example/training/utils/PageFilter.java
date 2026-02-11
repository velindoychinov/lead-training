package org.example.training.utils;

import org.springframework.data.domain.Sort;

public interface PageFilter {

  Integer getPage();
  Integer getSize();
  String getSort();
  Sort.Direction getDirection();

}
