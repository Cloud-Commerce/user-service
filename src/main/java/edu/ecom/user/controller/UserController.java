package edu.ecom.user.controller;

import edu.ecom.user.entity.User;
import edu.ecom.user.service.UserDetailsServiceImpl;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserDetailsServiceImpl userDetailsService;

  @Autowired
  public UserController(UserDetailsServiceImpl userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @GetMapping
  public ResponseEntity<?> getUsers(@RequestParam(required = false) String username) {
    List<User> users = Optional.ofNullable(username).map(userDetailsService::loadUserByUsername)
        .map(List::of).orElseGet(userDetailsService::loadAllUsers);
    return ResponseEntity.accepted().body(users);
  }

}
