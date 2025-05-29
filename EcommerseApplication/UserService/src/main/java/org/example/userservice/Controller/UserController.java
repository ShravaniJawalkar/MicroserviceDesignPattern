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
        UserResponse userResponse= usersService.registerUser(userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@Valid @RequestBody UserLoginRequest userRequest){
        UserResponse userResponse = usersService.loginUser(userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        UserResponse userResponse= usersService.getUserById(id);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
}
