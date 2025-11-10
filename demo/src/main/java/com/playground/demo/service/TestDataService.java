package com.playground.demo.service;

import com.playground.demo.dto.PlaySiteResponse;
import com.playground.demo.mapper.PlaySiteMapper;
import com.playground.demo.model.AttractionEntity;
import com.playground.demo.model.AttractionType;
import com.playground.demo.model.ChildEntity;
import com.playground.demo.model.PlaySiteEntity;
import com.playground.demo.repository.PlaySiteRepository;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestDataService {

  private final PlaySiteRepository playSiteRepository;
  private final PlaySiteMapper playSiteMapper;

  @Autowired
  public TestDataService(PlaySiteRepository playSiteRepository, PlaySiteMapper playSiteMapper) {
    this.playSiteRepository = playSiteRepository;
    this.playSiteMapper = playSiteMapper;
  }

  @Transactional
  public List<PlaySiteResponse> loadTestData() {
    // Clear existing data if any
    playSiteRepository.deleteAll();

    // Play Site 1: Adventure Playground.
    PlaySiteEntity playSite1 = new PlaySiteEntity();
    playSite1.setName("Adventure Playground");
    playSite1.setAttractions(Arrays.asList(
        new AttractionEntity(AttractionType.DOUBLE_SWING, 2),
        new AttractionEntity(AttractionType.SLIDE, 1),
        new AttractionEntity(AttractionType.BALL_PIT, 2)
    ));
    // Total capacity: (2*2) + (1*1) + (6*2) = 4 + 1 + 12 = 17.

    // Add children to play site 1 (some in attractions).
    playSite1.addChild(new ChildEntity("T001", "Alice", 5));
    playSite1.addChild(new ChildEntity("T002", "Bob", 6));
    playSite1.addChild(new ChildEntity("T003", "Charlie", 7));
    playSite1.addChild(new ChildEntity("T004", "Diana", 5));
    playSite1.addChild(new ChildEntity("T005", "Eve", 6));

    // Add children to queue for play site 1
    playSite1.addToQueue(new ChildEntity("T006", "Frank", 7));
    playSite1.addToQueue(new ChildEntity("T007", "Grace", 5));

    playSiteRepository.save(playSite1);

    // Play Site 2: Carousel Fun Zone
    PlaySiteEntity playSite2 = new PlaySiteEntity();
    playSite2.setName("Carousel Fun Zone");
    playSite2.setAttractions(Arrays.asList(
        new AttractionEntity(AttractionType.CAROUSEL, 2),
        new AttractionEntity(AttractionType.DOUBLE_SWING, 1),
        new AttractionEntity(AttractionType.SLIDE, 2)
    ));
    // Total capacity: (8*2) + (2*1) + (1*2) = 16 + 2 + 2 = 20.

    // Add children to play site 2 (some in attractions).
    playSite2.addChild(new ChildEntity("T008", "Henry", 8));
    playSite2.addChild(new ChildEntity("T009", "Ivy", 6));
    playSite2.addChild(new ChildEntity("T010", "Jack", 7));
    playSite2.addChild(new ChildEntity("T011", "Kate", 5));
    playSite2.addChild(new ChildEntity("T012", "Liam", 6));
    playSite2.addChild(new ChildEntity("T013", "Mia", 7));
    playSite2.addChild(new ChildEntity("T014", "Noah", 5));
    playSite2.addChild(new ChildEntity("T015", "Olivia", 6));
    playSite2.addChild(new ChildEntity("T016", "Paul", 7));
    playSite2.addChild(new ChildEntity("T017", "Quinn", 5));

    // Add children to queue for play site 2.
    playSite2.addToQueue(new ChildEntity("T018", "Rachel", 6));
    playSite2.addToQueue(new ChildEntity("T019", "Sam", 7));
    playSite2.addToQueue(new ChildEntity("T020", "Tina", 5));

    playSiteRepository.save(playSite2);

    // Play Site 3: Ball Pit Paradise
    PlaySiteEntity playSite3 = new PlaySiteEntity();
    playSite3.setName("Ball Pit Paradise");
    playSite3.setAttractions(Arrays.asList(
        new AttractionEntity(AttractionType.BALL_PIT, 3),
        new AttractionEntity(AttractionType.CAROUSEL, 1),
        new AttractionEntity(AttractionType.DOUBLE_SWING, 2)
    ));
    // Total capacity: (6*3) + (8*1) + (2*2) = 18 + 8 + 4 = 30

    // Add children to play site 3 (some in attractions).
    playSite3.addChild(new ChildEntity("T021", "Uma", 6));
    playSite3.addChild(new ChildEntity("T022", "Victor", 7));
    playSite3.addChild(new ChildEntity("T023", "Wendy", 5));
    playSite3.addChild(new ChildEntity("T024", "Xander", 6));
    playSite3.addChild(new ChildEntity("T025", "Yara", 7));
    playSite3.addChild(new ChildEntity("T026", "Zoe", 5));
    playSite3.addChild(new ChildEntity("T027", "Alex", 6));
    playSite3.addChild(new ChildEntity("T028", "Ben", 7));

    // Add children to queue for play site 3.
    playSite3.addToQueue(new ChildEntity("T029", "Cara", 5));
    playSite3.addToQueue(new ChildEntity("T030", "David", 6));
    playSite3.addToQueue(new ChildEntity("T031", "Emma", 7));
    playSite3.addToQueue(new ChildEntity("T032", "Finn", 5));

    playSiteRepository.save(playSite3);

    // Return all created play sites.
    return playSiteRepository.findAll().stream()
        .map(playSiteMapper::toResponse)
        .toList();
  }
}

