package com.playground.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "children")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "playSite")
@ToString(exclude = "playSite")
public class ChildEntity {

  @Id
  @Column(unique = true, nullable = false)
  private String ticketNumber;

  private String name;
  private Integer age;

  @Column(name = "entry_time")
  private LocalDateTime entryTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "play_site_id")
  private PlaySiteEntity playSite;

  public ChildEntity(String ticketNumber, String name, Integer age) {
    this.ticketNumber = ticketNumber;
    this.name = name;
    this.age = age;
    this.entryTime = LocalDateTime.now();
  }
}
