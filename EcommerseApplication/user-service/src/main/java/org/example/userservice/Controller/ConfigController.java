package org.example.userservice.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigController {

    @Value("${my-custom-property}")
    private String customProperty;

    @GetMapping("/config/test")
    public String getCustomProperties(){
        return customProperty;
    }
}
