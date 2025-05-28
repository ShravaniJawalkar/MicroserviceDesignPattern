package org.example.userservice.Dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistrationRequest {
    @NotNull
    @JsonProperty("user_name")
    private String username;
    @NotNull
    @JsonProperty("email")
    private String email;
    @NotNull
    @JsonProperty("password")
    private String password;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("address")
    private String address;

}
