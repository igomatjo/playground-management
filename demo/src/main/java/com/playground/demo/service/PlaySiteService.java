package com.playground.demo.service;

import com.playground.demo.dto.AddChildRequest;
import com.playground.demo.dto.AddChildResponse;
import com.playground.demo.dto.CreatePlaySiteRequest;
import com.playground.demo.dto.PlaySiteResponse;
import com.playground.demo.mapper.PlaySiteMapper;
import com.playground.demo.model.ChildEntity;
import com.playground.demo.model.PlaySiteEntity;
import com.playground.demo.repository.PlaySiteRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaySiteService {

  private final PlaySiteRepository playSiteRepository;
  private final PlaySiteMapper playSiteMapper;

  @Autowired
  public PlaySiteService(PlaySiteRepository playSiteRepository, PlaySiteMapper playSiteMapper) {
    this.playSiteRepository = playSiteRepository;
    this.playSiteMapper = playSiteMapper;
  }

  public PlaySiteResponse createPlaySite(CreatePlaySiteRequest request) {
    PlaySiteEntity playSite = new PlaySiteEntity();
    playSite.setName(request.getName());
    playSite.setAttractions(request.getAttractions());
    playSite = playSiteRepository.save(playSite);
    return playSiteMapper.toResponse(playSite);
  }

  public Optional<PlaySiteResponse> updatePlaySite(Long id, CreatePlaySiteRequest request) {
    Optional<PlaySiteEntity> optionalPlaySite = playSiteRepository.findById(id);
    if (optionalPlaySite.isEmpty()) {
      return Optional.empty();
    }

    PlaySiteEntity playSite = optionalPlaySite.get();

    // Check if new capacity is less than children in play site.
    int newCapacity = request.getAttractions().stream()
        .mapToInt(attraction -> attraction.getType().getMaxCapacity() * attraction.getQuantity())
        .sum();

    if (playSite.getChildren().size() > newCapacity) {
      throw new IllegalArgumentException("Cannot reduce capacity below current number of children: " + playSite.getChildren().size());
    }

    playSite.setName(request.getName());
    playSite.setAttractions(request.getAttractions());

    // Process queue if there's space now.
    while (!playSite.isFull() && !playSite.getQueue().isEmpty()) {
      playSite.processQueue();
    }

    playSiteRepository.save(playSite);
    return Optional.of(playSiteMapper.toResponse(playSite));
  }

  public Optional<PlaySiteResponse> getPlaySite(Long id) {
    return playSiteRepository.findById(id)
        .map(playSiteMapper::toResponse);
  }

  public List<PlaySiteResponse> getAllPlaySites() {
    return playSiteRepository.findAll().stream()
        .map(playSiteMapper::toResponse)
        .toList();
  }

  public boolean deletePlaySite(Long id) {
    if (!playSiteRepository.existsById(id)) {
      return false;
    }
    playSiteRepository.deleteById(id);
    return true;
  }

  public AddChildResponse addChildToPlaySite(Long playSiteId, AddChildRequest request) {
    Optional<PlaySiteEntity> optionalPlaySite = playSiteRepository.findById(playSiteId);
    if (optionalPlaySite.isEmpty()) {
      return new AddChildResponse(false, "Play site not found", false);
    }

    PlaySiteEntity playSite = optionalPlaySite.get();
    ChildEntity child = new ChildEntity(request.getTicketNumber(), request.getName(), request.getAge());

    // Check if child is already in this or another play site.
    Optional<PlaySiteEntity> existingPlaySite = playSiteRepository.findPlaySiteByTicketNumber(child.getTicketNumber());
    if (existingPlaySite.isPresent()) {
      if (existingPlaySite.get().getId().equals(playSiteId)) {
        return new AddChildResponse(false, "Child is already in this play site", false);
      } else {
        return new AddChildResponse(false, "Child is already in another play site", false);
      }
    }

    if (playSite.isFull()) {
      if (request.getAcceptQueue() != null && request.getAcceptQueue()) {
        playSite.addToQueue(child);
        playSiteRepository.save(playSite);
        return new AddChildResponse(true, "Play site is full. Child added to queue.", true);
      } else {
        return new AddChildResponse(false, "Play site is full and child does not accept waiting in queue", false);
      }
    }

    playSite.addChild(child);
    playSiteRepository.save(playSite);
    return new AddChildResponse(true, "Child added to play site", false);
  }

  public boolean removeChildFromPlaySite(String ticketNumber) {
    Optional<PlaySiteEntity> optionalPlaySite = playSiteRepository.findPlaySiteByTicketNumber(ticketNumber);
    if (optionalPlaySite.isEmpty()) {
      return false;
    }

    PlaySiteEntity playSite = optionalPlaySite.get();
    boolean removed = playSite.removeChild(ticketNumber);

    if (!removed) {
      // Try removing from queue.
      removed = playSite.removeChildFromQueue(ticketNumber);
    }

    if (removed) {
      // Process queue if there's space now.
      playSite.processQueue();
      playSiteRepository.save(playSite);
    }
    return removed;
  }

  public Optional<Integer> getPlaySiteUtilization(Long id) {
    return playSiteRepository.findById(id)
        .map(PlaySiteEntity::getCurrentUtilization);
  }

  public int getTotalVisitorsToday() {
    return playSiteRepository.getTotalVisitorsToday();
  }
}

