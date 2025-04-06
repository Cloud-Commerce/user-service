package edu.ecom.user.internal.controller;

import edu.ecom.user.dto.ChangePasswordRequest;
import edu.ecom.user.dto.CreateUserRequest;
import edu.ecom.user.dto.MessageResponse;
import edu.ecom.user.dto.UserDetailsDto;
import edu.ecom.user.dto.VerifyCredentialsRequest;
import edu.ecom.user.service.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

  @PostMapping("/add-user")
  public ResponseEntity<?> addUser(@Valid @RequestBody CreateUserRequest request) {
    try {
      userDetailsService.registerUser(request.getUsername(), request.getPassword(), request.getRoles());
      return ResponseEntity.accepted().body(new MessageResponse("User registered successfully!"));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/verified-user")
  public ResponseEntity<?> getVerifiedUser(@RequestBody VerifyCredentialsRequest request) {
    try {
      UserDetailsDto verifiedUser = userDetailsService.getVerifiedUser(request.getUsername(), request.getPassword());
      return ResponseEntity.ok(verifiedUser);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/change-password")
  public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
    try {
      userDetailsService.changePassword(request.getUsername(), request.getOldPassword(), request.getNewPassword());
      return ResponseEntity.accepted().body(new MessageResponse("Password changed successfully!"));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

}
