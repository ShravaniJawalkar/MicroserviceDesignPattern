package org.example.userservice.Service;

import org.example.userservice.Dao.UserRegistrationRequest;
import org.example.userservice.Dao.UserResponse;
import org.example.userservice.Dao.UserLoginRequest;
import org.example.userservice.Dao.Users;
import org.example.userservice.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsersService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UserResponse registerUser(UserRegistrationRequest request) {
        // Validate the request
        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        userRepository.save(user);
        UserResponse response = null;
        if (user.getId() == null) {
            response = new UserResponse();
            response.setResponseCode("500");
            return response;
        }
        response = new UserResponse();
        response.setUserId(user.getId());
        response.setUserName(user.getUsername());
        response.setResponseCode("200");
        return response;
    }

    public UserResponse getUserById(Long id) {
        Users user = userRepository.findById(id).orElse(null);

        UserResponse response = null;
        if (user == null) {
            response = new UserResponse();
            response.setResponseCode("404");
            return response;
        }
        response = new UserResponse();
        response.setUserId(user.getId());
        response.setUserName(user.getUsername());
        response.setResponseCode("200");
        return response;
    }
    @Transactional
    public UserResponse loginUser(UserLoginRequest request) {
        Users user = userRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword());
        UserResponse response = null;
        if (user == null) {
            response = new UserResponse();
            response.setResponseCode("404");
            return response;
        }
        response = new UserResponse();
        response.setUserId(user.getId());
        response.setUserName(user.getUsername());
        response.setResponseCode("200");
        return response;
    }
}
