package edu.ecom.user.service;

import edu.ecom.user.entity.User;
import edu.ecom.user.model.Role;
import edu.ecom.user.repository.UserRepository;
import jakarta.ws.rs.NotFoundException;
import java.util.InputMismatchException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailsServiceImpl {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public User loadUserByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new NotFoundException("User not found"));
  }

  public void registerUser(String username, String password, List<Role> roles) {
    if (userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException("Username already exists");
    }

    User user = new User(username, passwordEncoder.encode(password), roles);
    userRepository.save(user);
  }

  public Boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  public List<User> loadAllUsers() {
    return userRepository.findAll();
  }

  public void changePassword(String username, String oldPassword, String newPassword) {
    User user = loadUserByUsername(username);
    if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
      throw new InputMismatchException("Current password is invalid");
    }

    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
  }

  public Boolean verifyCredentials(String username, String password) {
    User user = loadUserByUsername(username);
    return passwordEncoder.matches(password, user.getPassword());
  }
}