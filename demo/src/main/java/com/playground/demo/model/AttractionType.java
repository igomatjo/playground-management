package com.playground.demo.model;

import lombok.Getter;

@Getter
public enum AttractionType {
  BALL_PIT("Ball pit", 6),
  CAROUSEL("Carousel", 8),
  DOUBLE_SWING("Double swing", 2),
  SLIDE("Slide", 1);

  private final String name;
  private final int maxCapacity;

  AttractionType(String name, int maxCapacity) {
    this.maxCapacity = maxCapacity;
    this.name = name;
  }
}

