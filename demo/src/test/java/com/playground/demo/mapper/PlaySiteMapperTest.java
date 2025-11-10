package com.playground.demo.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.playground.demo.dto.PlaySiteResponse;
import com.playground.demo.model.AttractionEntity;
import com.playground.demo.model.AttractionType;
import com.playground.demo.model.ChildEntity;
import com.playground.demo.model.PlaySiteEntity;
import java.util.List;
import org.junit.jupiter.api.Test;

class PlaySiteMapperTest {

  private final PlaySiteMapper subject = new PlaySiteMapperImpl();

  @Test
  void shouldMapToResponse() {
    // Given.
    PlaySiteEntity playSite = createPlaySite();
    PlaySiteResponse expected = createResponse(playSite);
    // When.
    PlaySiteResponse actual = subject.toResponse(playSite);
    // Then.
    assertEquals(expected, actual);
  }

  private PlaySiteEntity createPlaySite() {
    ChildEntity child = new ChildEntity("PS1", "Tony", 10);

    PlaySiteEntity playSite = new PlaySiteEntity(-1L, "Play site");
    playSite.setAttractions(List.of(new AttractionEntity(AttractionType.CAROUSEL, 3)));
    playSite.addChild(child);
    playSite.addToQueue(child);
    return playSite;
  }

  private PlaySiteResponse createResponse(PlaySiteEntity playSite) {
    return PlaySiteResponse.builder()
        .id(playSite.getId())
        .name(playSite.getName())
        .attractions(playSite.getAttractions())
        .children(playSite.getChildren())
        .queue(playSite.getQueue())
        .totalCapacity(playSite.getTotalCapacity())
        .currentUtilization(playSite.getCurrentUtilization())
        .totalVisitorsToday(playSite.getTotalVisitorsToday())
        .build();
  }
}