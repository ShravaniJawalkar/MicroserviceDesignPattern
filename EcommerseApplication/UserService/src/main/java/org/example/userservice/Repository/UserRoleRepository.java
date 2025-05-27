package org.example.userservice.Repository;

import org.example.userservice.Dao.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, String> {
    // UserRole findByName(String name);
}
