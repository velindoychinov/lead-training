package org.example.training.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.training.utils.PageFilter;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyGroupFilter implements PageFilter {

  private String groupName;

  private Integer page;
  private Integer size;
  private String sort;
  private Sort.Direction direction;

}
