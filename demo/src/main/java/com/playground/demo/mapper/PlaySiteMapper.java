package com.playground.demo.mapper;

import com.playground.demo.dto.PlaySiteResponse;
import com.playground.demo.model.ChildEntity;
import com.playground.demo.model.PlaySiteEntity;
import com.playground.demo.model.QueueEntry;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlaySiteMapper {

  PlaySiteResponse toResponse(PlaySiteEntity playSite);

  // Exclude play site from nested mapping.
  @Mapping(target = "playSite", ignore = true)
  ChildEntity toChildEntity(ChildEntity child);

  List<ChildEntity> toChildEntities(List<ChildEntity> children);

  // Exclude play site from nested mapping.
  @Mapping(target = "playSite", ignore = true)
  QueueEntry toQueueEntry(QueueEntry queueEntry);

  List<QueueEntry> toQueueEntries(List<QueueEntry> queueEntries);
}
