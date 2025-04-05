package edu.ecom.user.internal.controller;

import edu.ecom.user.dto.UserRequest;
import edu.ecom.user.dto.UserServiceResponse;
import edu.ecom.user.service.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/auth")
@Tag(name = "Internal Auth API", description = "FOR AUTH SERVICE USE ONLY")
@Hidden // Hide from public Swagger docs
public class AuthInternalController {

  private final UserDetailsServiceImpl userDetailsService;

  public AuthInternalController(UserDetailsServiceImpl userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @PostMapping("/verify")
  public ResponseEntity<?> verify(@RequestBody UserRequest request) {
    try {
      // Process credential verification
      Boolean verified = userDetailsService.verifyCredentials(request.getUsername(), request.getPassword());
      return ResponseEntity.ok(new UserServiceResponse(verified, null, null, null));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

}
