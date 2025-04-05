package edu.ecom.user.repository;

import edu.ecom.user.entity.UserRole;
import edu.ecom.user.model.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {}
