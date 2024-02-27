package com.bankflow.controllers;

import com.bankflow.dtos.RegistrationRequest;
import com.bankflow.exceptions.DublicateDataException;
import com.bankflow.exceptions.NegativeBalanceException;
import com.bankflow.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
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
        catch (DublicateDataException | NegativeBalanceException err)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
        }
    }

    @PatchMapping("/set-full-name")
    public ResponseEntity<?> setFullName (@RequestBody String fullName, @RequestParam UUID userId)
    {
        userService.setFullName(fullName, userId);
        return ResponseEntity.status(HttpStatus.OK).body("Nice to meet you, " + fullName);
    }

    @PatchMapping("/set-date-of-birth")
    public ResponseEntity<?> setDateOfBirth (@RequestBody String dateOfBirth, @RequestParam UUID userId)
    {
        try
        {
            userService.setDateOfBirth(dateOfBirth, userId);
            return ResponseEntity.status(HttpStatus.OK).body("The date of birth has been successfully set");
        }
        catch (ParseException err)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error date format");
        }
    }

}
