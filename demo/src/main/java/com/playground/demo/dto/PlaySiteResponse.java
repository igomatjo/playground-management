package com.playground.demo.dto;

import com.playground.demo.model.AttractionEntity;
import com.playground.demo.model.ChildEntity;
import com.playground.demo.model.QueueEntry;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlaySiteResponse {

  private Long id;
  private String name;
  private List<AttractionEntity> attractions;
  private List<ChildEntity> children;
  private List<QueueEntry> queue;
  private int totalCapacity;
  private int currentUtilization;
  private int totalVisitorsToday;
}

