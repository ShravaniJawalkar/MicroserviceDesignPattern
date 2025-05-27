package org.example.userservice.Repository;

import org.example.userservice.Dao.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {

    Users findByUsernameAfterAndPassword(String usernameAfter, String password);
}
