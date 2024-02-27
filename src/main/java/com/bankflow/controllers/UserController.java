package com.bankflow.controllers;

import com.bankflow.dtos.RegistrationRequest;
import com.bankflow.exceptions.DublicateDataException;
import com.bankflow.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody RegistrationRequest request)
    {
        try
        {
            userService.createUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("User successfully created");
        }
        catch (DublicateDataException err)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
        }
    }

}
