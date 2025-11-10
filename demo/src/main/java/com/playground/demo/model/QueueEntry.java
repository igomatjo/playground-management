package com.playground.demo.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "queue_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "playSite")
@ToString(exclude = "playSite")
public class QueueEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "child_ticket_number", nullable = false)
  private ChildEntity child;

  @Column(name = "added", nullable = false)
  private LocalDateTime addedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "play_site_id", nullable = false)
  private PlaySiteEntity playSite;

  public QueueEntry(ChildEntity child, PlaySiteEntity playSite) {
    this.child = child;
    this.playSite = playSite;
    this.addedAt = LocalDateTime.now();
  }
}
