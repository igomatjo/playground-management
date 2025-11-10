package com.playground.demo.dto;

import lombok.Data;

@Data
public class AddChildRequest {

  private String name;
  private Integer age;
  private String ticketNumber;
  private Boolean acceptQueue;
}

