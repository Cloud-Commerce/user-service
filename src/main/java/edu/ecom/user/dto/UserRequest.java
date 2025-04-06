package edu.ecom.user.dto;

import edu.ecom.user.model.Role;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Data;

@Data
public class UserRequest {

  @NotBlank(message = "Username cannot be blank")
  private String username;

  @NotBlank(message = "Password cannot be blank")
  private String password;

  private List<Role> roles;
}
