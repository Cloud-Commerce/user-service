package edu.ecom.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePasswordRequest {

  private String username;

  @NotBlank(message = "Old Password cannot be blank")
  private String oldPassword;

  @NotBlank(message = "New Password cannot be blank")
  private String newPassword;

}