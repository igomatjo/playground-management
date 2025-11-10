package com.playground.demo.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class PlaySiteExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(Exception ex) {
    log.error("Exception occurred ", ex);
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setError("Internal Server Error");
    errorResponse.setMessage(ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @Data
  private static class ErrorResponse {

    private String error;
    private String message;
  }
}

