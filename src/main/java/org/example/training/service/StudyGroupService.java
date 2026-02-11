package org.example.training.service;

import lombok.RequiredArgsConstructor;
import org.example.training.dto.filter.StudyGroupFilter;
import org.example.training.model.StudyGroup;
import org.example.training.repository.StudyGroupRepository;
import org.example.training.utils.ServiceUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.example.training.utils.ServiceUtils.anyLike;
@Service
@Transactional
@RequiredArgsConstructor
public class StudyGroupService {

  private final StudyGroupRepository studyGroupRepository;

  public StudyGroup createStudyGroup(StudyGroup studyGroup) {
    studyGroup.setGroupId(null);
    return studyGroupRepository.save(studyGroup);
  }

  public StudyGroup updateStudyGroup(StudyGroup changes) {
    StudyGroup entity = studyGroupRepository
      .findById(changes.getGroupId())
      .orElseThrow(() -> new IllegalStateException("StudyGroup not found"));

    entity.setGroupName(changes.getGroupName());

    return studyGroupRepository.save(entity);
  }

  public void deleteStudyGroup(Integer id) {
    StudyGroup studyGroup = studyGroupRepository.findById(id)
      .orElseThrow(() -> new IllegalStateException("StudyGroup not found"));
    studyGroupRepository.delete(studyGroup);
  }

  @Transactional(readOnly = true)
  public StudyGroup getStudyGroupById(Integer id) {
    return studyGroupRepository.findById(id).orElseThrow(() -> new IllegalStateException("StudyGroup not found"));
  }

  final List<Sort.Order> defaultSort = List.of(
    Sort.Order.asc(StudyGroup.Fields.groupName),
    Sort.Order.desc(StudyGroup.Fields.groupId)
  );

  @Transactional(readOnly = true)
  public Page<StudyGroup> findStudyGroup(StudyGroupFilter filter) {
    PageRequest paging = ServiceUtils.paging(filter, defaultSort);

    Specification<StudyGroup> spec = (root, query, cb) -> cb.conjunction();

    spec = spec.and(anyLike(filter.getGroupName(), r -> r.get(StudyGroup.Fields.groupName)));

    return studyGroupRepository.findAll(spec, paging);
  }
  
}
