package org.example.userservice.Service;

import org.example.userservice.Dao.UserRegistrationRequest;
import org.example.userservice.Dao.UserResponse;
import org.example.userservice.Dao.UserLoginRequest;
import org.example.userservice.Dao.Users;
import org.example.userservice.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class UsersService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ResponseEntity<UserResponse> registerUser(UserRegistrationRequest request) {
        // Validate the request
        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        userRepository.save(user);
        if (user.getId() == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        UserResponse response = new UserResponse();
        response.setUserId(user.getId());
        response.setUserName(user.getUsername());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    public ResponseEntity<UserResponse> getUserById(Long id) {
        Users user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserResponse response = new UserResponse();
        response.setUserId(user.getId());
        response.setUserName(user.getUsername());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<UserResponse> loginUser(UserLoginRequest request) {
        Users user = userRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword());
        UserResponse response = null;
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        response = new UserResponse();
        response.setUserId(user.getId());
        response.setUserName(user.getUsername());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
