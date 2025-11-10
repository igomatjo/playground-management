package com.playground.demo.dto;

import com.playground.demo.model.AttractionEntity;
import java.util.List;
import lombok.Data;

@Data
public class CreatePlaySiteRequest {

  private String name;
  private List<AttractionEntity> attractions;
}

