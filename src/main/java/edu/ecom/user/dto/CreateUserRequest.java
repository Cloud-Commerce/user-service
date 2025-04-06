package edu.ecom.user.dto;

import edu.ecom.user.model.Role;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserRequest {

  @NotBlank(message = "Username cannot be blank")
  private String username;

  @NotBlank(message = "Password cannot be blank")
  private String password;

  private List<Role> roles;
}