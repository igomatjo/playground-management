package com.playground.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttractionEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "attraction_type")
  private AttractionType type;

  private int quantity;

  public int getTotalCapacity() {
    return type.getMaxCapacity() * quantity;
  }
}
