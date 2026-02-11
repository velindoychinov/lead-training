package org.example.training.utils;

import java.util.List;

public class PageResponse<T> {

  private List<T> content;

  public List<T> getContent() {
    return content;
  }

  public void setContent(List<T> content) {
    this.content = content;
  }
}
