package org.example.userservice.Controller;

import jakarta.validation.Valid;
import org.example.userservice.Dao.UserLoginRequest;
import org.example.userservice.Dao.UserRegistrationRequest;
import org.example.userservice.Dao.UserResponse;
import org.example.userservice.Service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UsersService usersService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegistrationRequest userRequest){
        System.out.println("Received user: " + userRequest.getUsername());
        return usersService.registerUser(userRequest);
    }
    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@Valid @RequestBody UserLoginRequest userRequest){
        return  usersService.loginUser(userRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        return usersService.getUserById(id);
    }
}
