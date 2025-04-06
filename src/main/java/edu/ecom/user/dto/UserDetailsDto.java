package edu.ecom.user.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDetailsDto {

  private Long id;
  private String username;
  private List<String> roles;

}
