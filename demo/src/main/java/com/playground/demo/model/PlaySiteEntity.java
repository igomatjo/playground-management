package com.playground.demo.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "play_sites")
public class PlaySiteEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @ElementCollection
  @CollectionTable(name = "play_site_attractions", joinColumns = @JoinColumn(name = "play_site_id"))
  private List<AttractionEntity> attractions;

  @OneToMany(mappedBy = "playSite", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ChildEntity> children;

  @OneToMany(mappedBy = "playSite", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("addedAt ASC")
  private List<QueueEntry> queue;

  @Column(name = "total_visitors_today", nullable = false)
  private int totalVisitorsToday = 0;

  public PlaySiteEntity() {
    this.attractions = new ArrayList<>();
    this.children = new ArrayList<>();
    this.queue = new ArrayList<>();
  }

  public PlaySiteEntity(Long id, String name) {
    this();
    this.id = id;
    this.name = name;
  }

  public int getTotalCapacity() {
    return attractions.stream()
        .mapToInt(AttractionEntity::getTotalCapacity)
        .sum();
  }

  public int getCurrentUtilization() {
    int capacity = getTotalCapacity();
    if (capacity == 0) {
      return 0;
    }
    return (int) Math.round((double) children.size() / capacity * 100);
  }

  public boolean isFull() {
    return children.size() >= getTotalCapacity();
  }

  public void addChild(ChildEntity child) {
    child.setPlaySite(this);
    children.add(child);
    totalVisitorsToday++;
  }

  public boolean removeChild(String ticketNumber) {
    Optional<ChildEntity> first = children.stream()
        .filter(child -> child.getTicketNumber().equals(ticketNumber))
        .findFirst();
    first.ifPresent(child -> child.setPlaySite(null));
    return first.isPresent();
  }

  public boolean removeChildFromQueue(String ticketNumber) {
    return queue.removeIf(entry -> entry.getChild().getTicketNumber().equals(ticketNumber));
  }

  public void addToQueue(ChildEntity child) {
    QueueEntry entry = new QueueEntry(child, this);
    queue.add(entry);
  }

  public void processQueue() {
    if (!queue.isEmpty() && !isFull()) {
      QueueEntry entry = queue.removeFirst();
      addChild(entry.getChild());
    }
  }
}
