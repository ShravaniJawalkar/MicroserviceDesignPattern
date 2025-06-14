package org.example.userservice.Dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class UserLoginRequest {
    @NotNull
    @JsonProperty("user_name")
    private String username;
    @NotNull
    @JsonProperty("password")
    private String password;
}
