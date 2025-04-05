package edu.ecom.user.entity;

import edu.ecom.user.model.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Required by JPA
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(nullable = false, unique = true)
  private String username;

  @Setter
  @NotNull
  @Column(nullable = false)
  private String password;

  @Column(name = "created_at", nullable = false, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private final Date createdAt = new Date();

  @Setter
  @Column(name = "updated_at", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedAt = new Date();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<UserRole> roles = new HashSet<>();

  @Builder
  public User(@NotNull String username, @NotNull String password, @NotNull List<Role> roles) {
    this.username = username;
    this.password = password;
    this.roles = roles.stream().map(r -> new UserRole(this, r)).collect(Collectors.toSet());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username); // Only hash-immutable fields
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof User user)) return false;
    return Objects.equals(id, user.id) ||
        Objects.equals(username, user.username);
  }

  // Helper methods
  public void addRole(@NotNull Role role) {
    if (this.roles == null) {
      this.roles = new HashSet<>();
    }
    this.roles.add(new UserRole(this, role));
  }

  public void removeRole(@NotNull Role role) {
    roles.removeIf(userRole -> userRole.getRole() == role);
  }

}