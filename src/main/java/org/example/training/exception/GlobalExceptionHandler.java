package org.example.training.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleAllOtherExceptions(Exception ex) {
    log.error("Unhandled Exception", ex);
    Map<String, String> body = new HashMap<>();
    body.put("type", ex.getClass().getName());
    body.put("message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(body);
  }

}