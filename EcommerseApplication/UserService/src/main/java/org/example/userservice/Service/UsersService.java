package org.example.userservice.Service;

import lombok.NonNull;
import org.example.userservice.Dao.UserRegistrationRequest;
import org.example.userservice.Dao.UserResponse;
import org.example.userservice.Dao.UserLoginRequest;
import org.example.userservice.Dao.Users;
import org.example.userservice.Dao.UserRole;
import org.example.userservice.Dao.Role;
import org.example.userservice.Repository.UserRepository;
import org.example.userservice.Repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class UsersService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    public UserResponse registerUser(UserRegistrationRequest request) {
        // Validate the request
        Users user = new Users();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        addRoleToUser(user, request.getRole());
        userRepository.save(user);
        UserResponse response = null;
        if (user.getId() == null) {
            response = new UserResponse();
            response.setResponseCode("500");
            return response;
        }
        response = new UserResponse();
        response.setUserName(user.getUsername());
        response.setResponseCode("200");
        return response;
    }

    private void addRoleToUser(Users user, @NonNull String role) {
        Set<UserRole> userRoles = new HashSet<>();
        UserRole userRole = new UserRole();
        userRole.setRole(Role.valueOf(role));
        userRoles.add(userRole);
        user.setRoles(userRoles);
    }


    public UserResponse getUserById(UUID id) {
        Users user = userRepository.findById(id).orElse(null);

        UserResponse response = null;
        if (user == null) {
            response = new UserResponse();
            response.setResponseCode("404");
            return response;
        }
        response = new UserResponse();
        response.setResponseCode("200");
        return response;
    }

    public UserResponse loginUser(UserLoginRequest request) {
        Users user = userRepository.findByUsernameAfterAndPassword(request.getUsername(), request.getPassword());
        UserResponse response = null;
        if (user == null) {
            response = new UserResponse();
            response.setResponseCode("404");
            return response;
        }
        response = new UserResponse();
        response.setResponseCode("200");
        return response;
    }
}
