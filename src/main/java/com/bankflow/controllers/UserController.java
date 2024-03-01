package com.bankflow.controllers;

import com.bankflow.dtos.CustomUserDetails;
import com.bankflow.dtos.RegistrationRequest;
import com.bankflow.exceptions.DublicateDataException;
import com.bankflow.exceptions.NegativeBalanceException;
import com.bankflow.exceptions.UserDataChangeException;
import com.bankflow.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

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

    //TODO имя и дату рождения менять нельзя
    @PatchMapping("/set-full-name")
    public ResponseEntity<?> setFullName (@RequestParam String fullName,
                                          @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        try
        {
            userService.setFullName(fullName, userDetails.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body("Nice to meet you, " + fullName);
        }
        catch (UserDataChangeException err)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
        }

    }

    @PatchMapping("/set-date-of-birth")
    public ResponseEntity<?> setDateOfBirth (@RequestParam String dateOfBirth,
                                             @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        try
        {
            userService.setDateOfBirth(dateOfBirth, userDetails.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body("The date of birth has been successfully set");
        }
        catch (UserDataChangeException err)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
        }
        catch (ParseException err)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error date format.");
        }
    }

}
