package com.playground.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.playground.demo.dto.AddChildRequest;
import com.playground.demo.dto.AddChildResponse;
import com.playground.demo.dto.CreatePlaySiteRequest;
import com.playground.demo.mapper.PlaySiteMapper;
import com.playground.demo.model.AttractionEntity;
import com.playground.demo.model.AttractionType;
import com.playground.demo.repository.PlaySiteRepository;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PlaySiteServiceTest {

  private PlaySiteService subject;
  @Mock
  private PlaySiteRepository repository;
  @Mock
  private PlaySiteMapper mapper;

  private AutoCloseable openMocks;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    subject = new PlaySiteService(repository, mapper);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Test
  void createPlaySite() {
    // Given.
    CreatePlaySiteRequest request = new CreatePlaySiteRequest();
    request.setName("Test Playground");
    request.setAttractions(Arrays.asList(
        new AttractionEntity(AttractionType.DOUBLE_SWING, 2),
        new AttractionEntity(AttractionType.SLIDE, 1)
    ));
    // When.
    var response = subject.createPlaySite(request);
    // Then.
    assertNotNull(response);
    assertEquals("Test Playground", response.getName());
    assertEquals(5, response.getTotalCapacity()); // 2*2 + 1*1 = 5
  }

  @Test
  void addChildToPlaySite() {
    // Given.
    CreatePlaySiteRequest createRequest = new CreatePlaySiteRequest();
    createRequest.setName("Test");
    createRequest.setAttractions(Collections.singletonList(new AttractionEntity(AttractionType.DOUBLE_SWING, 1)));
    var playSite = subject.createPlaySite(createRequest);

    AddChildRequest addChildRequest = new AddChildRequest();
    addChildRequest.setTicketNumber("TICKET-001");
    addChildRequest.setName("Alice");
    addChildRequest.setAge(5);
    addChildRequest.setAcceptQueue(false);
    // When.
    AddChildResponse response = subject.addChildToPlaySite(playSite.getId(), addChildRequest);
    // Then.
    assertTrue(response.isSuccess());
    assertFalse(response.isQueued());
  }

  @Test
  void addChildWhenPlaySiteFull_WithQueueAcceptance() {
    // Given.
    CreatePlaySiteRequest createRequest = new CreatePlaySiteRequest();
    createRequest.setName("Test");
    createRequest.setAttractions(Collections.singletonList(new AttractionEntity(AttractionType.DOUBLE_SWING, 1)));
    var playSite = subject.createPlaySite(createRequest);

    AddChildRequest child1 = new AddChildRequest();
    child1.setTicketNumber("TICKET-001");
    child1.setName("Child1");
    child1.setAge(5);
    child1.setAcceptQueue(false);
    subject.addChildToPlaySite(playSite.getId(), child1);

    AddChildRequest child2 = new AddChildRequest();
    child2.setTicketNumber("TICKET-002");
    child2.setName("Child2");
    child2.setAge(6);
    child2.setAcceptQueue(false);
    subject.addChildToPlaySite(playSite.getId(), child2);

    AddChildRequest child3 = new AddChildRequest();
    child3.setTicketNumber("TICKET-003");
    child3.setName("Child3");
    child3.setAge(7);
    child3.setAcceptQueue(true);
    // When.
    AddChildResponse response = subject.addChildToPlaySite(playSite.getId(), child3);
    // Then.
    assertTrue(response.isSuccess());
    assertTrue(response.isQueued());
  }

  @Test
  void removeChild_ProcessesQueue() {
    // Given.
    CreatePlaySiteRequest createRequest = new CreatePlaySiteRequest();
    createRequest.setName("Test");
    createRequest.setAttractions(Collections.singletonList(new AttractionEntity(AttractionType.DOUBLE_SWING, 1)));
    var playSite = subject.createPlaySite(createRequest);

    AddChildRequest child1 = new AddChildRequest();
    child1.setTicketNumber("TICKET-001");
    child1.setName("Child1");
    child1.setAge(5);
    child1.setAcceptQueue(false);
    subject.addChildToPlaySite(playSite.getId(), child1);

    AddChildRequest child2 = new AddChildRequest();
    child2.setTicketNumber("TICKET-002");
    child2.setName("Child2");
    child2.setAge(6);
    child2.setAcceptQueue(false);
    subject.addChildToPlaySite(playSite.getId(), child2);

    // Add child to queue
    AddChildRequest child3 = new AddChildRequest();
    child3.setTicketNumber("TICKET-003");
    child3.setName("Child3");
    child3.setAge(7);
    child3.setAcceptQueue(true);
    subject.addChildToPlaySite(playSite.getId(), child3);
    // When.
    var playSiteAfterQueue = subject.getPlaySite(playSite.getId());
    // Then verify child3 is in queue.
    assertTrue(playSiteAfterQueue.isPresent());
    assertEquals(1, playSiteAfterQueue.get().getQueue().size());

    // When remove child1.
    boolean removed = subject.removeChildFromPlaySite("TICKET-001");
    // Then.
    assertTrue(removed);
    // When.
    var playSiteAfterRemoval = subject.getPlaySite(playSite.getId());
    // Then verify child3 moved from queue to play site.
    assertTrue(playSiteAfterRemoval.isPresent());
    assertEquals(0, playSiteAfterRemoval.get().getQueue().size());
    assertEquals(2, playSiteAfterRemoval.get().getChildren().size());
    assertTrue(playSiteAfterRemoval.get().getChildren().stream()
        .anyMatch(k -> k.getTicketNumber().equals("TICKET-003")));
  }

  @Test
  void getUtilization() {
    // Given.
    CreatePlaySiteRequest createRequest = new CreatePlaySiteRequest();
    createRequest.setName("Test");
    createRequest.setAttractions(Collections.singletonList(new AttractionEntity(AttractionType.DOUBLE_SWING, 1)));
    var playSite = subject.createPlaySite(createRequest);

    // Add 1 child to play site with capacity 2 and expect utilization 50%.
    AddChildRequest child1 = new AddChildRequest();
    child1.setTicketNumber("TICKET-001");
    child1.setName("Child1");
    child1.setAge(5);
    child1.setAcceptQueue(false);
    subject.addChildToPlaySite(playSite.getId(), child1);
    // When.
    var utilization = subject.getPlaySiteUtilization(playSite.getId());
    // Then.
    assertTrue(utilization.isPresent());
    assertEquals(50, utilization.get());
  }
}

