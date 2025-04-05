package edu.ecom.user.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
  // Getters
  private LocalDateTime timestamp;
  private int status;
  private String error;
  private String message;
  private String path;

}