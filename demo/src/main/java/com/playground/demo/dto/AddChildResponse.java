package com.playground.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddChildResponse {

  private boolean success;
  private String message;
  private boolean queued;
}

